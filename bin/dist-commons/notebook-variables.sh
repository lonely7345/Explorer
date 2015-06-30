#!/bin/bash

if [ "x$NOTEBOOK_HOME" == "x" ] ; then
    export NOTEBOOK_HOME="/etc/sds/notebook"
fi

if [ "x$NOTEBOOK_CONF_DIR" == "x" ] ; then
    export NOTEBOOK_CONF_DIR="$NOTEBOOK_HOME"
fi

if [ "x$NOTEBOOK_LOG_DIR" == "x" ]; then
    export NOTEBOOK_LOG_DIR="/var/log/sds/notebook"
fi

if [ "x$NOTEBOOK_NOTEBOOK_DIR" == "x" ]; then
    export NOTEBOOK_NOTEBOOK_DIR="$NOTEBOOK_HOME/notebook"
fi

if [ "x$NOTEBOOK_PID_DIR" == "x" ]; then
    export NOTEBOOK_PID_DIR="/var/run/sds"
fi

if [ "x$NOTEBOOK_WAR" == "x" ]; then
    if [ -d "${NOTEBOOK_HOME}/zeppelin-web/src/main/webapp" ]; then
	    export NOTEBOOK_WAR="${NOTEBOOK_HOME}/zeppelin-web/src/main/webapp"
    else
        export NOTEBOOK_WAR=`find ${NOTEBOOK_HOME} -name "zeppelin-web*.war"`
    fi
fi

if [ "x$NOTEBOOK_API_WAR" == "x" ]; then
    if [ -d "${NOTEBOOK_HOME}/zeppelin-docs/src/main/swagger" ]; then
	    export NOTEBOOK_API_WAR="${NOTEBOOK_HOME}/zeppelin-docs/src/main/swagger"
    else
        export NOTEBOOK_API_WAR=`find ${NOTEBOOK_HOME}/ -name "zeppelin-api-ui*.war"`
    fi
fi

if [ "x$NOTEBOOK_INTERPRETER_DIR" == "x" ]; then
    export NOTEBOOK_INTERPRETER_DIR="$NOTEBOOK_HOME/interpreter"
fi


if [ -f "${NOTEBOOK_CONF_DIR}/notebook-env.sh" ]; then
    . "${NOTEBOOK_CONF_DIR}/notebook-env.sh"
fi

NOTEBOOK_CLASSPATH+=":${NOTEBOOK_CONF_DIR}"



if [ -d "${NOTEBOOK_HOME}/zeppelin-zengine/target/classes" ]; then
    NOTEBOOK_CLASSPATH+=":${NOTEBOOK_HOME}/zeppelin-zengine/target/classes"
fi

if [ -d "${NOTEBOOK_HOME}/zeppelin-server/target/classes" ]; then
    NOTEBOOK_CLASSPATH+=":${NOTEBOOK_HOME}/zeppelin-server/target/classes"
fi


if [ "x$SPARK_HOME" != "x" ] && [ -d "${SPARK_HOME}" ]; then
    addJarInDir "${SPARK_HOME}"
fi

if [ "x$HADOOP_HOME" != "x" ] && [ -d "${HADOOP_HOME}" ]; then
    addJarInDir "${HADOOP_HOME}"
fi

export NOTEBOOK_CLASSPATH
export SPARK_CLASSPATH+=${NOTEBOOK_CLASSPATH}
export CLASSPATH+=${NOTEBOOK_CLASSPATH}

# Text encoding for
# read/write job into files,
# receiving/displaying query/result.
if [ "x$NOTEBOOK_ENCODING" == "x" ]; then
  export NOTEBOOK_ENCODING="UTF-8"
fi

if [ "x$NOTEBOOK_MEM" == "x" ]; then
  export NOTEBOOK_MEM="-Xmx1024m -XX:MaxPermSize=512m"
fi


JAVA_OPTS+="$NOTEBOOK_JAVA_OPTS -Dfile.encoding=${NOTEBOOK_ENCODING} ${NOTEBOOK_MEM}"
export JAVA_OPTS

if [ -n "$JAVA_HOME" ]; then
    NOTEBOOK_RUNNER="${JAVA_HOME}/bin/java"
else
    NOTEBOOK_RUNNER=java
fi

export RUNNER


if [ "x$NOTEBOOK_IDENT_STRING" == "x" ]; then
  export NOTEBOOK_IDENT_STRING="$USER"
fi

if [ "x$DEBUG" == "x" ] ; then
    export DEBUG=0
fi