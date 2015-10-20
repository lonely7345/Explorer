/*
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
package com.stratio.explorer.interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.explorer.conf.ExplorerConfiguration;
import com.stratio.explorer.conf.ExplorerConfiguration.ConfVars;

public class InterpreterFactory {
    Logger logger = LoggerFactory.getLogger(InterpreterFactory.class);

    private Map<String, Object> share = Collections.synchronizedMap(new HashMap<String, Object>());
    private Map<String, URLClassLoader> cleanCl = Collections.synchronizedMap(new HashMap<String, URLClassLoader>());

    private ExplorerConfiguration conf;
    String[] interpreterClassList;
    String defaultInterpreterName;

    public InterpreterFactory(ExplorerConfiguration conf) {
        this.conf = conf;
        String replsConf = conf.getString(ConfVars.EXPLORER_INTERPRETERS);
        interpreterClassList = replsConf.split(",");

        init();
    }

    private void init() {
        ClassLoader oldcl = Thread.currentThread().getContextClassLoader();

        File[] interpreterDirs = new File(conf.getInterpreterDir()).listFiles();
        if (interpreterDirs != null) {
            for (File path : interpreterDirs) {
                logger.info("Reading " + path.getAbsolutePath());
                URL[] urls = null;
                try {
                    urls = recursiveBuildLibList(path);
                } catch (MalformedURLException e1) {
                    logger.error("Can't load jars ", e1);
                }
                URLClassLoader ccl = new URLClassLoader(urls, oldcl);

                for (String className : interpreterClassList) {
                    try {
                        Class.forName(className, true, ccl);
                        Set<String> keys = Interpreter.registeredInterpreters.keySet();
                        for (String intName : keys) {
                            if (className.equals(Interpreter.registeredInterpreters.get(intName))) {
                                logger.info("Interpreter " + intName + " found. class=" + className);
                                cleanCl.put(intName, ccl);

                                if (className.equals(interpreterClassList[0])) {
                                    defaultInterpreterName = intName;
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        // nothing to do
                    }
                }
            }
        }
    }

    public String getDefaultInterpreterName() {
        return defaultInterpreterName;
    }

    public Interpreter createRepl(String replName, Properties properties) {
        String className = Interpreter.registeredInterpreters.get(replName != null ? replName : defaultInterpreterName);
        logger.info("find repl class {} = {}", replName, className);
        if (className == null) {
            logger.error("Configuration not found for " + replName);
            throw new RuntimeException("Configuration not found for " + replName);
        }
        return createRepl(replName, className, properties);
    }

    public Interpreter createRepl(String dirName, String className, Properties property) {
        logger.info("Create repl {} from {}", className, dirName);

        ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
        try {

            URLClassLoader ccl = cleanCl.get(dirName);
            if (ccl == null) {
                // classloader fallback
                ccl = URLClassLoader.newInstance(new URL[] {}, oldcl);
            }

            boolean separateCL = true;
            try { // check if server's classloader has driver already.
                Class cls = this.getClass().forName(className);
                if (cls != null) {
                    separateCL = false;
                }
            } catch (Exception e) {
                // nothing to do.
            }

            URLClassLoader cl;

            if (separateCL == true) {
                cl = URLClassLoader.newInstance(new URL[] {}, ccl);
            } else {
                cl = (URLClassLoader) ccl;
            }
            Thread.currentThread().setContextClassLoader(cl);

            Class<Interpreter> replClass = (Class<Interpreter>) cl.loadClass(className);
            Constructor<Interpreter> constructor = replClass.getConstructor(new Class[] { Properties.class });
            Interpreter repl = constructor.newInstance(property);
            if (conf.containsKey("args")) {
                property.put("args", conf.getProperty("args"));
            }
            property.put("share", share);
            property.put("classloaderUrls", ccl.getURLs());
            return new ClassloaderInterpreter(repl, cl, property);
        } catch (SecurityException e) {
            throw new InterpreterException(e);
        } catch (NoSuchMethodException e) {
            throw new InterpreterException(e);
        } catch (IllegalArgumentException e) {
            throw new InterpreterException(e);
        } catch (InstantiationException e) {
            throw new InterpreterException(e);
        } catch (IllegalAccessException e) {
            throw new InterpreterException(e);
        } catch (InvocationTargetException e) {
            throw new InterpreterException(e);
        } catch (ClassNotFoundException e) {
            throw new InterpreterException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldcl);
        }
    }

    public String loadCrossdataSettings(){
        return loadFromFile(conf.getCrossdataSettingsPath());
    }


    public void saveCrossdataSettings(String file) throws IOException {

        saveToFile(file, conf.getCrossdataSettingsPath());
    }

    public void loadCrossdataDefaultSettings() throws IOException {
        saveToFile(loadFromFile(conf.getCrossdataDefaultSettingsPath()), conf.getCrossdataSettingsPath());
    }

    public void saveEditorSettings(String body,String path ) throws IOException {
        saveToFile(body,path);
    }
    public String loadEditorData(String path){
        return loadFromFile(path);
    }


    //TODO : THIS METHOD WILL BE REMOVED
    private String loadFromFile(String path) {

        File fileToRead = new File(path);
        if (!fileToRead.exists()) {
            // nothing to read
            return "empty";
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileToRead);

        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            isr.close();
            fis.close();
            return sb.toString();

        } catch (FileNotFoundException e) {
            return "input stream error " + e;
        } catch (IOException e) {
            return "io exception " + e;
        }

    }

    //TODO : THIS METHOD WILL BE REMOVED
    private void saveToFile(String file, String path) throws IOException {
        File settingFile = new File(path);
        if (!settingFile.exists()) {
            settingFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(settingFile, false);
        OutputStreamWriter out = new OutputStreamWriter(fos);
        out.append(file);
        out.close();
        fos.close();
    }

    private URL[] recursiveBuildLibList(File path) throws MalformedURLException {
        URL[] urls = new URL[0];
        if (path == null || path.exists() == false) {
            return urls;
        } else if (path.getName().startsWith(".")) {
            return urls;
        } else if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File f : files) {
                    urls = (URL[]) ArrayUtils.addAll(urls, recursiveBuildLibList(f));
                }
            }
            return urls;
        } else {
            return new URL[] { path.toURI().toURL() };
        }
    }
}
