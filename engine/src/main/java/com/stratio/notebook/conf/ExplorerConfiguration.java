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
package com.stratio.notebook.conf;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExplorerConfiguration extends XMLConfiguration {
    private static final String EXPLORER_SITE_XML = "explorer-site.xml";
	private static final long serialVersionUID = -8830696863101855773L;
    private static final Logger LOG = LoggerFactory.getLogger(ExplorerConfiguration.class);
	private static ExplorerConfiguration conf;

    public ExplorerConfiguration(URL url) throws ConfigurationException {
		setDelimiterParsingDisabled(true);
		load(url);
	}

	public ExplorerConfiguration() {
		ConfVars[] vars = ConfVars.values();
		for(ConfVars v : vars){
			if(v.getType()==ConfVars.VarType.BOOLEAN){
				this.setProperty(v.getVarName(), v.getBooleanValue());
			} else if(v.getType()==ConfVars.VarType.LONG){
				this.setProperty(v.getVarName(), v.getLongValue());
			} else if(v.getType()==ConfVars.VarType.INT){
				this.setProperty(v.getVarName(), v.getIntValue());
			} else if(v.getType()==ConfVars.VarType.FLOAT){
				this.setProperty(v.getVarName(), v.getFloatValue());
			} else if(v.getType()==ConfVars.VarType.STRING){
				this.setProperty(v.getVarName(), v.getStringValue());
			} else {
				throw new RuntimeException("Unsupported VarType");
			}
		}
	}

	/**
	 * Load from resource
	 * @throws ConfigurationException
	 */
	public static ExplorerConfiguration create() {
		if (conf != null) return conf;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url;

		url = ExplorerConfiguration.class.getResource(EXPLORER_SITE_XML);
		if (url == null) {
			 ClassLoader cl = ExplorerConfiguration.class.getClassLoader();
			 if (cl!=null) {
				 url = cl.getResource(EXPLORER_SITE_XML);
			 }
		}
		if (url == null) {
			url = classLoader.getResource(EXPLORER_SITE_XML);
		}

		if (url == null){
            LOG.warn("Failed to load configuration, proceeding with a default");
		    conf =  new ExplorerConfiguration();
		} else {
			try {
				LOG.info("Load configuration from "+url);
			    conf = new ExplorerConfiguration(url);
			} catch (ConfigurationException e) {
			    LOG.warn("Failed to load configuration from " + url + " proceeding with a default", e);
			    conf = new ExplorerConfiguration();
			}
		}

		return conf;
	}


	private String getStringValue(String name, String d){
		List<ConfigurationNode> properties = getRootNode().getChildren();
		if(properties==null || properties.size()==0) return d;
		for(ConfigurationNode p : properties){
			if(p.getChildren("name")!=null && p.getChildren("name").size()>0 && name.equals(p.getChildren("name").get(0).getValue())){
				return (String) p.getChildren("value").get(0).getValue();
			}
		}
		return d;
	}

	private int getIntValue(String name, int d){
		List<ConfigurationNode> properties = getRootNode().getChildren();
		if(properties==null || properties.size()==0) return d;
		for(ConfigurationNode p : properties){
			if(p.getChildren("name")!=null && p.getChildren("name").size()>0 && name.equals(p.getChildren("name").get(0).getValue())){
				return Integer.parseInt((String) p.getChildren("value").get(0).getValue());
			}
		}
		return d;
	}

	private long getLongValue(String name, long d){
		List<ConfigurationNode> properties = getRootNode().getChildren();
		if(properties==null || properties.size()==0) return d;
		for(ConfigurationNode p : properties){
			if(p.getChildren("name")!=null && p.getChildren("name").size()>0 && name.equals(p.getChildren("name").get(0).getValue())){
				return Long.parseLong((String) p.getChildren("value").get(0).getValue());
			}
		}
		return d;
	}

	private float getFloatValue(String name, float d){
		List<ConfigurationNode> properties = getRootNode().getChildren();
		if(properties==null || properties.size()==0) return d;
		for(ConfigurationNode p : properties){
			if(p.getChildren("name")!=null && p.getChildren("name").size()>0 && name.equals(p.getChildren("name").get(0).getValue())){
				return Float.parseFloat((String) p.getChildren("value").get(0).getValue());
			}
		}
		return d;
	}

	private boolean getBooleanValue(String name, boolean d){
		List<ConfigurationNode> properties = getRootNode().getChildren();
		if(properties==null || properties.size()==0) return d;
		for(ConfigurationNode p : properties){
			if(p.getChildren("name")!=null && p.getChildren("name").size()>0 && name.equals(p.getChildren("name").get(0).getValue())){
				return Boolean.parseBoolean((String) p.getChildren("value").get(0).getValue());
			}
		}
		return d;
	}

	public String getString(ConfVars c){
		return getString(c.name(), c.getVarName(), c.getStringValue());
	}

	public String getString(String envName, String propertyName, String defaultValue){
		if(System.getenv(envName)!=null){
			return System.getenv(envName);
		}
		if(System.getProperty(propertyName)!=null){
			return System.getProperty(propertyName);
		}

		return getStringValue(propertyName, defaultValue);
	}

	public int getInt(ConfVars c){
		return getInt(c.name(), c.getVarName(), c.getIntValue());
	}

	public int getInt(String envName, String propertyName, int defaultValue){
		if(System.getenv(envName)!=null){
			return Integer.parseInt(System.getenv(envName));
		}

		if(System.getProperty(propertyName)!=null){
			return Integer.parseInt(System.getProperty(propertyName));
		}
		return getIntValue(propertyName, defaultValue);
	}

	public long getLong(ConfVars c){
		return getLong(c.name(), c.getVarName(), c.getLongValue());
	}

	public long getLong(String envName, String propertyName, long defaultValue){
		if(System.getenv(envName)!=null){
			return Long.parseLong(System.getenv(envName));
		}

		if(System.getProperty(propertyName)!=null){
			return Long.parseLong(System.getProperty(propertyName));
		}
		return getLongValue(propertyName, defaultValue);
	}

	public float getFloat(ConfVars c){
		return getFloat(c.name(), c.getVarName(), c.getFloatValue());
	}
	public float getFloat(String envName, String propertyName, float defaultValue){
		if(System.getenv(envName)!=null){
			return Float.parseFloat(System.getenv(envName));
		}
		if(System.getProperty(propertyName)!=null){
			return Float.parseFloat(System.getProperty(propertyName));
		}
		return getFloatValue(propertyName, defaultValue);
	}


	public boolean getBoolean(ConfVars c){
		return getBoolean(c.name(), c.getVarName(), c.getBooleanValue());
	}
	public boolean getBoolean(String envName, String propertyName, boolean defaultValue){
		if(System.getenv(envName)!=null){
			return Boolean.parseBoolean(System.getenv(envName));
		}

		if(System.getProperty(propertyName)!=null){
			return Boolean.parseBoolean(System.getProperty(propertyName));
		}
		return getBooleanValue(propertyName, defaultValue);
	}

	public String getExplorerDir(){
		return getRelativeDir(ConfVars.EXPLORER_NOTEBOOK_DIR);
	}

	public String getInterpreterDir(){
		return getRelativeDir(ConfVars.EXPLORER_INTERPRETER_DIR);
	}

	public String getRelativeDir(ConfVars c){
		return getRelativeDir(getString(c));
	}

	public String getRelativeDir(String path){
		if(path!=null && path.startsWith("/")){
			return path;
		} else {
			return getString(ConfVars.EXPLORER_HOME)+"/"+path;
		}
	}


	//TODO review how to recovered this variables better.

	/**
	 * The Explorer conf dir.
	 */
	private static final String EXPLORER_CONF_DIR = System.getenv().get("EXPLORER_CONF_DIR");
	/**
	 * The Crossdata settings path.
	 */
	private static String CROSSDATA_SETTINGS_PATH = EXPLORER_CONF_DIR + File.separator+"driver-application.conf";
	/**
	 * The Default Crossdata settings path.
	 */
	private static String CROSSDATA_DEFAULT_SETTINGS_PATH = EXPLORER_CONF_DIR + File.separator+"driver-application-default.conf";


	public String getCrossdataSettingsPath() {
		System.out.println(CROSSDATA_SETTINGS_PATH);
		return CROSSDATA_SETTINGS_PATH;
	}

	public String getCrossdataDefaultSettingsPath() {
		return CROSSDATA_DEFAULT_SETTINGS_PATH;
	}


	public static enum ConfVars {
		EXPLORER_HOME("zeppelin.home", "../"),
		EXPLORER_PORT("zeppelin.server.port", 8084),
		EXPLORER_WAR("zeppelin.war", "../web/src/main/webapp"),
	    EXPLORER_API_WAR("zeppelin.api.war", "../doc/src/main/swagger"),
		EXPLORER_INTERPRETERS("zeppelin.interpreters", "com.nflabs.zeppelin.spark.SparkInterpreter,com.nflabs.zeppelin.spark.SparkSqlInterpreter,com.nflabs.zeppelin.markdown.Markdown,com.nflabs.zeppelin.shell.ShellInterpreter"),
		EXPLORER_INTERPRETER_DIR("zeppelin.interpreter.dir", "interpreter"),
		EXPLORER_INTERPRETER_MODE("zeppelin.interpreter.mode", "share"),     // 'separate', 'share'
		EXPLORER_ENCODING("zeppelin.encoding", "UTF-8"),
		EXPLORER_NOTEBOOK_DIR("zeppelin.notebook.dir", "explorer")
		;

		private String varName;
		@SuppressWarnings("rawtypes")
        private Class varClass;
		private String stringValue;
		private VarType type;
		private int intValue;
		private float floatValue;
		private boolean booleanValue;
		private long longValue;


		ConfVars(String varName, String varValue){
			this.varName = varName;
			this.varClass = String.class;
			this.stringValue = varValue;
			this.intValue = -1;
			this.floatValue = -1;
			this.longValue = -1;
			this.booleanValue = false;
			this.type = VarType.STRING;
		}

		ConfVars(String varName, int intValue){
			this.varName = varName;
			this.varClass = Integer.class;
			this.stringValue = null;
			this.intValue = intValue;
			this.floatValue = -1;
			this.longValue = -1;
			this.booleanValue = false;
			this.type = VarType.INT;
		}

		ConfVars(String varName, long longValue){
			this.varName = varName;
			this.varClass = Integer.class;
			this.stringValue = null;
			this.intValue = -1;
			this.floatValue = -1;
			this.longValue = longValue;
			this.booleanValue = false;
			this.type = VarType.INT;
		}

		ConfVars(String varName, float floatValue){
			this.varName = varName;
			this.varClass = Float.class;
			this.stringValue = null;
			this.intValue = -1;
			this.longValue = -1;
			this.floatValue = floatValue;
			this.booleanValue = false;
			this.type = VarType.FLOAT;
		}

		ConfVars(String varName, boolean booleanValue){
			this.varName = varName;
			this.varClass = Boolean.class;
			this.stringValue = null;
			this.intValue = -1;
			this.longValue = -1;
			this.floatValue = -1;
			this.booleanValue = booleanValue;
			this.type = VarType.BOOLEAN;
		}

	    public String getVarName() {
			return varName;
		}

		@SuppressWarnings("rawtypes")
        public Class getVarClass() {
			return varClass;
		}

		public int getIntValue(){
			return intValue;
		}

		public long getLongValue(){
			return longValue;
		}

		public float getFloatValue(){
			return floatValue;
		}

		public String getStringValue() {
			return stringValue;
		}

		public boolean getBooleanValue(){
			return booleanValue;
		}

		public VarType getType() {
			return type;
		}

		enum VarType {
	        STRING { @Override
	        void checkType(String value) throws Exception { } },
	        INT { @Override
	        void checkType(String value) throws Exception { Integer.valueOf(value); } },
	        LONG { @Override
	        void checkType(String value) throws Exception { Long.valueOf(value); } },
	        FLOAT { @Override
	        void checkType(String value) throws Exception { Float.valueOf(value); } },
	        BOOLEAN { @Override
	        void checkType(String value) throws Exception { Boolean.valueOf(value); } };

	        boolean isType(String value) {
	          try { checkType(value); } catch (Exception e) { return false; }
	          return true;
	        }
	        String typeString() { return name().toUpperCase();}
	        abstract void checkType(String value) throws Exception;
	    }
	}

}
