#!/bin/bash
#
#/**
# * Copyright 2007 The Apache Software Foundation
# *
# * Licensed to the Apache Software Foundation (ASF) under one
# * or more contributor license agreements.  See the NOTICE file
# * distributed with this work for additional information
# * regarding copyright ownership.  The ASF licenses this file
# * to you under the Apache License, Version 2.0 (the
# * "License"); you may not use this file except in compliance
# * with the License.  You may obtain a copy of the License at
# *
# *     http://www.apache.org/licenses/LICENSE-2.0
# *
# * Unless required by applicable law or agreed to in writing, software
# * distributed under the License is distributed on an "AS IS" BASIS,
# * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# * See the License for the specific language governing permissions and
# * limitations under the License.
# */

FWDIR="$(cd `dirname $0`; pwd)"
INSTALL_HOME="/opt/sds/notebook/"

if [ "x$NOTEBOOK_HOME" == "x" ] ; then
    export NOTEBOOK_HOME=$FWDIR/..
fi

if [ "x$NOTEBOOK_CONF_DIR" == "x" ] ; then
    export NOTEBOOK_CONF_DIR="$NOTEBOOK_HOME/conf"
fi

if [ -d "$INSTALL_HOME" ]; then
    export NOTEBOOK_CONF_DIR="/etc/sds/notebook"
fi

if [ "x$NOTEBOOK_LOG_DIR" == "x" ]; then
    export NOTEBOOK_LOG_DIR="$NOTEBOOK_HOME/logs"
fi

if [ "x$NOTEBOOK_NOTEBOOK_DIR" == "x" ]; then
    export NOTEBOOK_NOTEBOOK_DIR="$NOTEBOOK_HOME/notebook"
fi

if [ "x$NOTEBOOK_PID_DIR" == "x" ]; then
    export NOTEBOOK_PID_DIR="$NOTEBOOK_HOME/run"
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

function addJarInDir(){
    if [ -d "${1}" ]; then
	for jar in `find ${1} -maxdepth 1 -name '*jar'`; do
	    NOTEBOOK_CLASSPATH=$jar:$NOTEBOOK_CLASSPATH
	done
    fi
}

addJarInDir ${NOTEBOOK_HOME}

addJarInDir ${NOTEBOOK_HOME}/zeppelin-zengine/target/lib
addJarInDir ${NOTEBOOK_HOME}/zeppelin-server/target/lib
addJarInDir ${NOTEBOOK_HOME}/zeppelin-web/target/lib
addJarInDir $INSTALL_HOME/lib


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

NOTEBOOK_LOGFILE=$NOTEBOOK_LOG_DIR/notebook-$NOTEBOOK_IDENT_STRING-$HOSTNAME.log

if [ -d "$INSTALL_HOME" ]; then
    NOTEBOOK_LOGFILE="/var/log/sds/notebook/notebook_app.log"
fi

if [ "x$DEBUG" == "x" ] ; then
    export DEBUG=0
fi
