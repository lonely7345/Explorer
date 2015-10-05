/*
*Licensed to STRATIO (C) under one or more contributor license agreements.
*See the NOTICE file distributed with this work for additional information
*regarding copyright ownership.  The STRATIO (C) licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
/**
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stratio.notebook.spark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.spark.HttpServer;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.SparkEnv;
import org.apache.spark.repl.SparkCommandLine;
import org.apache.spark.repl.SparkILoop;
import org.apache.spark.repl.SparkIMain;
import org.apache.spark.repl.SparkJLineCompletion;
import org.apache.spark.scheduler.ActiveJob;
import org.apache.spark.scheduler.DAGScheduler;
import org.apache.spark.scheduler.Stage;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.ui.jobs.JobProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.interpreter.InterpreterResult.Code;
import com.stratio.notebook.notebook.NoteInterpreterLoader;
import com.stratio.notebook.scheduler.Scheduler;
import com.stratio.notebook.scheduler.SchedulerFactory;
import com.stratio.notebook.spark.dep.DependencyResolver;

import scala.Console;
import scala.None;
import scala.Some;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;
import scala.collection.mutable.HashMap;
import scala.collection.mutable.HashSet;
import scala.tools.nsc.Settings;
import scala.tools.nsc.interpreter.Completion.Candidates;
import scala.tools.nsc.interpreter.Completion.ScalaCompleter;
import scala.tools.nsc.settings.MutableSettings.BooleanSetting;
import scala.tools.nsc.settings.MutableSettings.PathSetting;

public class SparkInterpreter extends Interpreter {
    Logger logger = LoggerFactory.getLogger(SparkInterpreter.class);

    static {
        Interpreter.register("s", SparkInterpreter.class.getName());
    }

    private ZeppelinContext z;
    private SparkILoop interpreter;
    private SparkIMain intp;
    private SparkContext sc;
    private ByteArrayOutputStream out;
    private SQLContext sqlc;
    private DependencyResolver dep;
    private SparkJLineCompletion completor;

    private JobProgressListener sparkListener;

    private Map<String, Object> binder;
    private SparkEnv env;

    static SparkInterpreter _singleton;

    public static SparkInterpreter singleton() {
        return _singleton;
    }

    public static SparkInterpreter singleton(Properties property) {
        if (_singleton == null) {
            new SparkInterpreter(property);
        }
        return _singleton;
    }

    public static void setSingleton(SparkInterpreter si) {
        _singleton = si;
    }

    public SparkInterpreter(Properties property) {
        super(property);
        out = new ByteArrayOutputStream();
        if (_singleton == null) {
            _singleton = this;
        }
    }

    public synchronized SparkContext getSparkContext() {
        Map<String, Object> share = (Map<String, Object>) getProperty().get("share");
        logger.info("SC in Map share " + share.containsKey("sc"));

        if (sc == null) {
            sc = (SparkContext) share.get("sc");
            sparkListener = (JobProgressListener) share.get("sparkListener");
            logger.info("sparkListener in Map share " + share.containsKey("sparkListener"));

            if (sc == null) {
                sc = createSparkContext();
                env = SparkEnv.get();
                sparkListener = new JobProgressListener(sc.getConf());
                sc.listenerBus().addListener(sparkListener);

				/* Sharing a single spark context across scala repl is not possible at the moment.
                 * because of spark's limitation.
				 *   1) Each SparkImain (scala repl) creates classServer but worker (executor uses only the first one)
				 *   2) creating a SparkContext creates corresponding worker's Executor. which executes tasks and reuse classloader.
				 *      the same Classloader can confuse classes from many different scala repl.
				 *      
				 * The code below is commented out until this limitation removes
				 */
                share.put("sc", sc);
                //share.put("sparkEnv", env);
                //share.put("sparkListener", sparkListener);
            }

        }

        return sc;
    }

    private boolean useHiveContext() {
        return Boolean.parseBoolean(System.getenv("NOTEBOOK_SPARK_USEHIVECONTEXT"));
    }

    public SQLContext getSQLContext() {
        if (sqlc == null) {
            if (useHiveContext()) {
                String name = "org.apache.spark.sql.hive.HiveContext";
                Constructor<?> hc;
                try {
                    hc = getClass().getClassLoader().loadClass(name)
                            .getConstructor(SparkContext.class);
                    sqlc = (SQLContext) hc.newInstance(getSparkContext());
                } catch (NoSuchMethodException | SecurityException
                        | ClassNotFoundException | InstantiationException
                        | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    logger.warn("Can't create HiveContext. Fallback to SQLContext", e);
                    // when hive dependency is not loaded, it'll fail.
                    // in this case SQLContext can be used.
                    sqlc = new SQLContext(getSparkContext());
                }
            } else {
                sqlc = new SQLContext(getSparkContext());
            }
        }

        return sqlc;
    }

    public DependencyResolver getDependencyResolver() {
        if (dep == null) {
            // save / load sc from common share
            Map<String, Object> share = (Map<String, Object>) getProperty().get("share");
            dep = (DependencyResolver) share.get("dep");
            if (dep == null) {
                dep = new DependencyResolver(intp, sc);
                //share.put("dep", dep);
            }
        }
        return dep;
    }

    public SparkContext createSparkContext() {
        logger.info("------ Create new SparkContext " + getMaster() + " -------");

        String execUri = System.getenv("SPARK_EXECUTOR_URI");
        String[] jars = SparkILoop.getAddedJars();

        String classServerUri = null;


        logger.info(interpreter.toString());
        logger.info(interpreter.intp().toString());
        try { // in case of spark 1.1x, spark 1.2x
            Method classServer = interpreter.intp().getClass().getMethod("classServer");
            HttpServer httpServer = (HttpServer) classServer.invoke(interpreter.intp());
            classServerUri = httpServer.uri();
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            // continue
        }

        if (classServerUri == null) {
            try { // for spark 1.3x, 1.4x
                Method classServer = interpreter.intp().getClass().getMethod("classServerUri");
                classServerUri = (String) classServer.invoke(interpreter.intp());
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                logger.error(e.getMessage());
            }
        }

        SparkConf conf =
                new SparkConf()
                        .setMaster(getMaster())
                        .setAppName("stratio-explorer")
                        .setJars(jars)
                        .set("spark.repl.class.uri", classServerUri);
        if (execUri != null) {
            conf.set("spark.executor.uri", execUri);
        }
        if (System.getenv("SPARK_HOME") != null) {
            conf.setSparkHome(System.getenv("SPARK_HOME"));
            System.out.println("##### SPARK_HOME: " + System.getenv("SPARK_HOME"));
        }
        conf.set("spark.scheduler.mode", "FAIR");

        return new SparkContext(conf);
    }

    public String getMaster() {
        String envMaster = System.getenv().get("MASTER");
        if (envMaster != null) {
            return envMaster;
        }
        String propMaster = System.getProperty("spark.master");
        if (propMaster != null) {
            return propMaster;
        }
        return "local[*]";
    }

    @Override
    public void open() {

//            Map<String, Object> share = (Map<String, Object>) getProperty().get("share");
            URL[] urls = (URL[]) getProperty().get("classloaderUrls");

            // Very nice discussion about how scala compiler handle classpath
            // https://groups.google.com/forum/#!topic/scala-user/MlVwo2xCCI0

		/*
         * > val env = new nsc.Settings(errLogger)
> env.usejavacp.value = true
> val p = new Interpreter(env)
> p.setContextClassLoader
>
Alternatively you can set the class path throuh nsc.Settings.classpath.

>> val settings = new Settings()
>> settings.usejavacp.value = true
>> settings.classpath.value += File.pathSeparator +
>> System.getProperty("java.class.path")
>> val in = new Interpreter(settings) {
>>    override protected def parentClassLoader = getClass.getClassLoader
>> }
>> in.setContextClassLoader()


		 */
            Settings settings = new Settings();
            if (getProperty().containsKey("args")) {
                SparkCommandLine command = new SparkCommandLine(
                        scala.collection.JavaConversions.asScalaBuffer((List<String>) getProperty().get("args"))
                                .toList());
                settings = command.settings();
            }

            // set classpath for scala compiler
            PathSetting pathSettings = settings.classpath();
            String classpath = "";
            List<File> paths = currentClassPath();
            for (File f : paths) {
                if (classpath.length() > 0) {
                    classpath += File.pathSeparator;
                }
                classpath += f.getAbsolutePath();
            }

            if (urls != null) {
                for (URL u : urls) {
                    if (classpath.length() > 0) {
                        classpath += File.pathSeparator;
                    }
                    classpath += u.getFile();
                }
            }

            pathSettings.v_$eq(classpath);
            settings.scala$tools$nsc$settings$ScalaSettings$_setter_$classpath_$eq(pathSettings);

            // set classloader for scala compiler
            settings.explicitParentLoader_$eq(new Some<ClassLoader>(Thread.currentThread().getContextClassLoader()));
            BooleanSetting b = (BooleanSetting) settings.usejavacp();
            b.v_$eq(true);
            settings.scala$tools$nsc$settings$StandardScalaSettings$_setter_$usejavacp_$eq(b);

            PrintStream printStream = new PrintStream(out);

		/* spark interpreter */
            this.interpreter = new SparkILoop(null, new PrintWriter(out));
            interpreter.settings_$eq(settings);

            interpreter.createInterpreter();

            intp = interpreter.intp();
            intp.setContextClassLoader();
            intp.initializeSynchronous();

            completor = new SparkJLineCompletion(intp);

            sc = getSparkContext();
            sqlc = getSQLContext();

            dep = getDependencyResolver();

            NoteInterpreterLoader noteInterpreterLoader = (NoteInterpreterLoader) getProperty().get("noteIntpLoader");
            z = new ZeppelinContext(sc, sqlc, dep, noteInterpreterLoader, printStream);
            System.out.println(z.sc.getConf().toDebugString());
            System.out.println(z.sqlContext.getAllConfs().mkString());

            try {
                if (sc.version().startsWith("1.1") || sc.version().startsWith("1.2")) {
                    Method loadFiles = this.interpreter.getClass().getMethod("loadFiles", Settings.class);
                    loadFiles.invoke(this.interpreter, settings);
                } else if (sc.version().startsWith("1.3")) {
                    Method loadFiles = this.interpreter.getClass().getMethod(
                            "org$apache$spark$repl$SparkILoop$$loadFiles", Settings.class);
                    loadFiles.invoke(this.interpreter, settings);
                } else if (sc.version().startsWith("1.4")) {
                    Method loadFiles = this.interpreter.getClass().getMethod(
                            "org$apache$spark$repl$SparkILoop$$loadFiles", Settings.class);
                    loadFiles.invoke(this.interpreter, settings);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                logger.error(e.getMessage());//TODO
                logger.info(e.getStackTrace().toString());
                System.out.println(e.getStackTrace());
            }

            intp.interpret("@transient var _binder = new java.util.HashMap[String, Object]()");
            binder = (Map<String, Object>) getValue("_binder");
            binder.put("sc", sc);
            binder.put("sqlc", sqlc);
            binder.put("z", z);
            binder.put("out", printStream);

            intp.interpret(
                    "@transient val z = _binder.get(\"z\").asInstanceOf[com.nflabs.zeppelin.spark.ZeppelinContext]");
            intp.interpret("@transient val sc = _binder.get(\"sc\").asInstanceOf[org.apache.spark.SparkContext]");
            intp.interpret("@transient val sqlc = _binder.get(\"sqlc\").asInstanceOf[org.apache.spark.sql.SQLContext]");
            intp.interpret(
                    "@transient val sqlContext = _binder.get(\"sqlc\").asInstanceOf[org.apache.spark.sql.SQLContext]");
            intp.interpret("import org.apache.spark.SparkContext._");
            intp.interpret("import sqlc._");

            if (sc.version().startsWith("1.1")) {
                intp.interpret("import sqlContext._");
            } else if (sc.version().startsWith("1.2")) {
                intp.interpret("import sqlContext._");
            } else if (sc.version().startsWith("1.3")) {
                intp.interpret("import sqlContext.implicits._");
                intp.interpret("import sqlContext.sql");
                intp.interpret("import org.apache.spark.sql.functions._");
            } else if (sc.version().startsWith("1.4")) {
                intp.interpret("import sqlContext.implicits._");
                intp.interpret("import sqlContext.sql");
                intp.interpret("import org.apache.spark.sql.functions._");
            }
    }

    private List<File> currentClassPath() {
        List<File> paths = classPath(Thread.currentThread().getContextClassLoader());
        String[] cps = System.getProperty("java.class.path").split(File.pathSeparator);
        if (cps != null) {
            for (String cp : cps) {
                paths.add(new File(cp));
            }
        }
        return paths;
    }

    private List<File> classPath(ClassLoader cl) {
        List<File> paths = new LinkedList<File>();
        if (cl == null) {
            return paths;
        }

        if (cl instanceof URLClassLoader) {
            URLClassLoader ucl = (URLClassLoader) cl;
            URL[] urls = ucl.getURLs();
            if (urls != null) {
                for (URL url : urls) {
                    paths.add(new File(url.getFile()));
                }
            }
        }
        return paths;
    }

    public List<String> completion(String buf, int cursor) {
        ScalaCompleter c = completor.completer();
        Candidates ret = c.complete(buf, cursor);
        return scala.collection.JavaConversions.asJavaList(ret.candidates());
    }

    public void bindValue(String name, Object o) {
    }

    public Object getValue(String name) {
        Object ret = intp.valueOfTerm(name);
        if (ret instanceof None) {
            return null;
        } else if (ret instanceof Some) {
            return ((Some) ret).get();
        } else {
            return ret;
        }
    }

    private final String jobGroup = "notebook-" + this.hashCode();

    /**
     * Interpret a single line
     */
    public InterpreterResult interpret(String line) {
        if (line == null || line.trim().length() == 0) {
            return new InterpreterResult(Code.SUCCESS);
        }
        return interpret(line.split("\n"));
    }

    public InterpreterResult interpret(String[] lines) {
        synchronized (this) {
            sc.setJobGroup(jobGroup, "Notebook", false);
            InterpreterResult r = _interpret(lines);
            sc.clearJobGroup();
            return r;
        }
    }

    public InterpreterResult _interpret(String[] lines) {
        //Map<String, Object> share = (Map<String, Object>)getProperty().get("share");
        //SparkEnv env = (SparkEnv) share.get("sparkEnv");
        SparkEnv.set(env);

        // add print("") to make sure not finishing with comment
        // see https://github.com/NFLabs/zeppelin/issues/151
        String[] linesToRun = new String[lines.length + 1];
        for (int i = 0; i < lines.length; i++) {
            linesToRun[i] = lines[i];
        }
        linesToRun[lines.length] = "print(\"\")";

        Console.setOut((java.io.PrintStream) binder.get("out"));
        out.reset();
        Code r = null;
        String incomplete = "";
        for (String s : linesToRun) {
            scala.tools.nsc.interpreter.Results.Result res = null;
            try {
                res = intp.interpret(incomplete + s);
            } catch (Exception e) {
                sc.clearJobGroup();
                logger.info("Interpreter exception", e);
                return new InterpreterResult(Code.ERROR, e.getMessage());
            }

            r = getResultCode(res);

            if (r == Code.ERROR) {
                sc.clearJobGroup();
                return new InterpreterResult(r, out.toString());
            } else if (r == Code.INCOMPLETE) {
                incomplete += s + "\n";
            } else {
                incomplete = "";
            }
        }

        if (r == Code.INCOMPLETE) {
            return new InterpreterResult(r, "Incomplete expression");
        } else {
            return new InterpreterResult(r, out.toString());
        }
    }

    public void cancel() {
        sc.cancelJobGroup(jobGroup);
    }

    public int getProgress() {
        int completedTasks = 0;
        int totalTasks = 0;

        DAGScheduler scheduler = sc.dagScheduler();
        if (scheduler == null) {
            return 0;
        }
        HashSet<ActiveJob> jobs = scheduler.activeJobs();
        if (jobs == null || jobs.size() == 0) {
            return 0;
        }
        Iterator<ActiveJob> it = jobs.iterator();
        while (it.hasNext()) {
            ActiveJob job = it.next();
            String g = (String) job.properties().get("spark.jobGroup.id");
            if (jobGroup.equals(g)) {
                int[] progressInfo = null;
                if (sc.version().startsWith("1.0")) {
                    progressInfo = getProgressFromStage_1_0x(sparkListener, job.finalStage());
                } else {
                    progressInfo = getProgressFromStage_1_1x(sparkListener, job.finalStage());
                }

                totalTasks += progressInfo[0];
                completedTasks += progressInfo[1];
            }
        }

        if (totalTasks == 0) {
            return 0;
        }
        return completedTasks * 100 / totalTasks;
    }

    private int[] getProgressFromStage_1_0x(JobProgressListener sparkListener, Stage stage) {
        int numTasks = stage.numTasks();
        int completedTasks = 0;

        Method method;
        Object completedTaskInfo = null;
        try {
            method = sparkListener.getClass().getMethod("stageIdToTasksComplete");
            completedTaskInfo = JavaConversions.asJavaMap((HashMap<Object, Object>) method.invoke(sparkListener))
                    .get(stage.id());
        } catch (NoSuchMethodException | SecurityException e) {
            logger.error("Error while getting progress", e);
        } catch (IllegalAccessException e) {
            logger.error("Error while getting progress", e);
        } catch (IllegalArgumentException e) {
            logger.error("Error while getting progress", e);
        } catch (InvocationTargetException e) {
            logger.error("Error while getting progress", e);
        }

        if (completedTaskInfo != null) {
            completedTasks += (int) completedTaskInfo;
        }
        List<Stage> parents = JavaConversions.asJavaList(stage.parents());
        if (parents != null) {
            for (Stage s : parents) {
                int[] p = getProgressFromStage_1_0x(sparkListener, s);
                numTasks += p[0];
                completedTasks += p[1];
            }
        }

        return new int[] { numTasks, completedTasks };
    }

    private int[] getProgressFromStage_1_1x(JobProgressListener sparkListener, Stage stage) {
        int numTasks = stage.numTasks();
        int completedTasks = 0;

        try {
            Method stageIdToData = sparkListener.getClass().getMethod("stageIdToData");
            HashMap<Tuple2<Object, Object>, Object> stageIdData = (HashMap<Tuple2<Object, Object>, Object>) stageIdToData
                    .invoke(sparkListener);
            Class<?> stageUIDataClass = this.getClass().forName("org.apache.spark.ui.jobs.UIData$StageUIData");

            Method numCompletedTasks = stageUIDataClass.getMethod("numCompleteTasks");

            Set<Tuple2<Object, Object>> keys = JavaConverters.asJavaSetConverter(stageIdData.keySet()).asJava();
            for (Tuple2<Object, Object> k : keys) {
                if (stage.id() == (int) k._1()) {
                    Object uiData = stageIdData.get(k).get();
                    completedTasks += (int) numCompletedTasks.invoke(uiData);
                }
            }
        } catch (Exception e) {
            logger.error("Error on getting progress information", e);
        }

        List<Stage> parents = JavaConversions.asJavaList(stage.parents());
        if (parents != null) {
            for (Stage s : parents) {
                int[] p = getProgressFromStage_1_1x(sparkListener, s);
                numTasks += p[0];
                completedTasks += p[1];
            }
        }
        return new int[] { numTasks, completedTasks };
    }

    private Code getResultCode(scala.tools.nsc.interpreter.Results.Result r) {
        if (r instanceof scala.tools.nsc.interpreter.Results.Success$) {
            return Code.SUCCESS;
        } else if (r instanceof scala.tools.nsc.interpreter.Results.Incomplete$) {
            return Code.INCOMPLETE;
        } else {
            return Code.ERROR;
        }
    }

    @Override
    public void close() {
        sc.stop();
        sc = null;

        intp.close();
    }

    @Override
    public FormType getFormType() {
        return FormType.NATIVE;
    }

    public JobProgressListener getJobProgressListener() {
        return sparkListener;
    }

    @Override
    public Scheduler getScheduler() {
        return SchedulerFactory.singleton()
                .createOrGetFIFOScheduler(SparkInterpreter.class.getName() + this.hashCode());
    }
}
