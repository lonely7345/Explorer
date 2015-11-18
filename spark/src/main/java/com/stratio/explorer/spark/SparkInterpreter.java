/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.spark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.stratio.explorer.reader.PropertiesReader;
import com.stratio.explorer.spark.gateways.contexts.ExplorerSparkContext;
import com.stratio.explorer.spark.gateways.contexts.ExplorerSparkSQLContext;
import com.stratio.explorer.spark.lists.SparkConfComparator;
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

import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.interpreter.InterpreterResult.Code;
import com.stratio.explorer.notebook.NoteInterpreterLoader;
import com.stratio.explorer.scheduler.Scheduler;
import com.stratio.explorer.scheduler.SchedulerFactory;

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
    /**
     * The log.
     */
    Logger logger = LoggerFactory.getLogger(SparkInterpreter.class);

    static {
        Interpreter.register("s", SparkInterpreter.class.getName());
    }

    private SparkILoop interpreter;
    private SparkIMain intp;
    private ByteArrayOutputStream out;
    //private SQLContext sqlc;
    private SparkJLineCompletion completor;

    private JobProgressListener sparkListener;

    private Map<String, Object> binder;
    private SparkEnv env;

    static SparkInterpreter _singleton;

    //TODO : THIS ATTR WAS NEW ADD
    private  ExplorerSparkContext context = new ExplorerSparkContext(new SparkConfComparator());
    private  ExplorerSparkSQLContext sqlcontext = new ExplorerSparkSQLContext(context);

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

    //TODO : PROPERTIES ALWAYS IS EMPTY
    public SparkInterpreter(Properties property) {
        super(property);
        out = new ByteArrayOutputStream();
        if (_singleton == null) {
            _singleton = this;
        }
    }

    public synchronized SparkContext getSparkContext() {
        context.loadConfiguration(new PropertiesReader().readConfigFrom("spark_interpreter"));
        SparkContext sc =context.getConnector();
        env = SparkEnv.get();
        sparkListener = new JobProgressListener(context.getConnector().getConf());
        sc.listenerBus().addListener(sparkListener);

        return sc;
    }


    //TODO : this method will be move to othre class
    public SQLContext getSQLContext() {
        sqlcontext.loadConfiguration(new PropertiesReader().readConfigFrom("spark_interpreter"));
        return sqlcontext.getConnector();
    }


    //TODO : THIS METHOD IS TOO LONG
    @Override
    public void open() {

            URL[] urls = (URL[]) getProperty().get("classloaderUrls");

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

            getSparkContext();
            getSQLContext();

            NoteInterpreterLoader noteInterpreterLoader = (NoteInterpreterLoader) getProperty().get("noteIntpLoader");
            logger.info(context.getConnector().getConf().toDebugString());
        logger.info(sqlcontext.getConnector().getAllConfs().mkString()); //SUSTITUTE BY

            try {
                if (context.getConnector().version().startsWith("1.1") || context.getConnector().version().startsWith("1.2")) {
                    Method loadFiles = this.interpreter.getClass().getMethod("loadFiles", Settings.class);
                    loadFiles.invoke(this.interpreter, settings);
                } else if (context.getConnector().version().startsWith("1.3")) {
                    Method loadFiles = this.interpreter.getClass().getMethod(
                            "org$apache$spark$repl$SparkILoop$$loadFiles", Settings.class);
                    loadFiles.invoke(this.interpreter, settings);
                } else if (context.getConnector().version().startsWith("1.4")) {
                    Method loadFiles = this.interpreter.getClass().getMethod(
                            "org$apache$spark$repl$SparkILoop$$loadFiles", Settings.class);
                    loadFiles.invoke(this.interpreter, settings);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                logger.error(e.getMessage());//TODO
                logger.info(e.getStackTrace().toString());

            }

        intp.interpret("@transient var _binder = new java.util.HashMap[String, Object]()");
            ExplorerContext explorerContext = new ExplorerContext(context.getConnector(), sqlcontext.getConnector(), noteInterpreterLoader, printStream);
           /****************************************************************************************************************************/
            binder = (Map<String, Object>) getValue("_binder");
            binder.put("sc", context.getConnector());
            binder.put("sqlc", sqlcontext.getConnector());
            binder.put("z", explorerContext);
            binder.put("out", printStream);

            intp.interpret(
                    "@transient val explorerContext = _binder.get(\"explorerContext\").asInstanceOf[com.stratio.explorer.spark.ExplorerContext]");
            intp.interpret("@transient val sc = _binder.get(\"sc\").asInstanceOf[org.apache.spark.SparkContext]");
            intp.interpret("@transient val sqlc = _binder.get(\"sqlc\").asInstanceOf[org.apache.spark.sql.SQLContext]");
            intp.interpret(
                    "@transient val sqlContext = _binder.get(\"sqlc\").asInstanceOf[org.apache.spark.sql.SQLContext]");
            intp.interpret("import org.apache.spark.SparkContext._");
            intp.interpret("import sqlc._");

            if (context.getConnector().version().startsWith("1.1")) {
                intp.interpret("import sqlContext._");
            } else if (context.getConnector().version().startsWith("1.2")) {
                intp.interpret("import sqlContext._");
            } else if (context.getConnector().version().startsWith("1.3")) {
                intp.interpret("import sqlContext.implicits._");
                intp.interpret("import sqlContext.sql");
                intp.interpret("import org.apache.spark.sql.functions._");
            } else if (context.getConnector().version().startsWith("1.4")) {
                intp.interpret("import sqlContext.implicits._");
                intp.interpret("import sqlContext.sql");
                intp.interpret("import org.apache.spark.sql.functions._");
            }
        /*************************************************************************************************************************************/
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

    //TODO : THIS METHODS SHOULD BE REMOVED
    public InterpreterResult interpret(String[] lines) {
        synchronized (this) {
            context.getConnector().setJobGroup(jobGroup, "Notebook", false);
            InterpreterResult r = _interpret(lines);
            context.getConnector().clearJobGroup();
            return r;
        }
    }

    public InterpreterResult _interpret(String[] lines) {

        SparkEnv.set(env);

        String[] linesToRun = new String[lines.length + 1];
        for (int i = 0; i < lines.length; i++) {
            linesToRun[i] = lines[i];
        }
        linesToRun[lines.length] = "print(\"\")";

      //  Console.setOut((java.io.PrintStream) binder.get("out"));
        out.reset();
        Code r = null;
        String incomplete = "";
        for (String s : linesToRun) {
            scala.tools.nsc.interpreter.Results.Result res = null;
            try {
                res = intp.interpret(incomplete + s);
            } catch (Exception e) {
                context.getConnector().clearJobGroup();
                logger.info("Interpreter exception", e);
                return new InterpreterResult(Code.ERROR, e.getMessage());
            }

            r = getResultCode(res);

            if (r == Code.ERROR) {
                context.getConnector().clearJobGroup();
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
        context.getConnector().cancelJobGroup(jobGroup);
    }

    public int getProgress() {
        int completedTasks = 0;
        int totalTasks = 0;

        DAGScheduler scheduler = context.getConnector().dagScheduler();
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
                if (context.getConnector().version().startsWith("1.0")) {
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
        context.getConnector().stop();
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
