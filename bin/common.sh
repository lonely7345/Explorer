#!/bin/bash
FWDIR="$(cd `dirname $0`; pwd)"
INSTALL_HOME="/opt/sds/explorer/"
LOG="TRUE"

if [ "x$EXPLORER_HOME" = "x" ] ; then
    export EXPLORER_HOME=$FWDIR/..
fi

if [ "x$EXPLORER_CONF_DIR" = "x" ] ; then
    export EXPLORER_CONF_DIR="$EXPLORER_HOME/conf"
fi


if [ -d "$INSTALL_HOME" ]; then
    export EXPLORER_CONF_DIR="/etc/sds/explorer"
fi

if [ "x$EXPLORER_LOG_DIR" = "x" ]; then
    export EXPLORER_LOG_DIR="$EXPLORER_HOME/logs"
fi


if [ "x$EXPLORER_DIR" = "x" ]; then
    export EXPLORER_DIR="$EXPLORER_HOME/explorer"
fi

if [ "x$EXPLORER_PID_DIR" = "x" ]; then
    export EXPLORER_PID_DIR="$EXPLORER_HOME/run"
fi

if [ "x$EXPLORER_WAR" = "x" ]; then
    if [ -d "${EXPLORER_HOME}/web/src/main/webapp" ]; then
	    export EXPLORER_WAR="${EXPLORER_HOME}/web/src/main/webapp"
    else
        export EXPLORER_WAR=`find ${EXPLORER_HOME} -name "explorer-web*.war"`
    fi
fi

if [ "x$EXPLORER_API_WAR" = "x" ]; then
    if [ -d "${EXPLORER_HOME}/doc/src/main/swagger" ]; then
	    export EXPLORER_API_WAR="${EXPLORER_HOME}/doc/src/main/swagger"
    else
        export EXPLORER_API_WAR=`find ${EXPLORER_HOME}/ -name "explorer-api-ui*.war"`
    fi
fi

if [ "x$EXPLORER_INTERPRETER_DIR" = "x" ]; then
    export EXPLORER_INTERPRETER_DIR="$EXPLORER_HOME/interpreter"
fi


if [ -f "${EXPLORER_CONF_DIR}/explorer-env.sh" ]; then
    . "${EXPLORER_CONF_DIR}/explorer-env.sh"
fi



EXPLORER_CLASSPATH+=":${EXPLORER_CONF_DIR}"

function addJarInDir(){
    if [ -d "${1}" ]; then
	for jar in `find ${1} -maxdepth 1 -name '*jar'`; do
	    EXPLORER_CLASSPATH=$jar:$EXPLORER_CLASSPATH
	done
    fi
}

addJarInDir ${EXPLORER_HOME}

addJarInDir ${EXPLORER_HOME}/engine/target/lib
addJarInDir ${EXPLORER_HOME}/server/target/lib
addJarInDir ${EXPLORER_HOME}/web/target/lib
addJarInDir $INSTALL_HOME/lib


if [ -d "${EXPLORER_HOME}/engine/target/classes" ]; then
    EXPLORER_CLASSPATH+=":${EXPLORER_HOME}/engine/target/classes"
fi

if [ -d "${EXPLORER_HOME}/server/target/classes" ]; then
    EXPLORER_CLASSPATH+=":${EXPLORER_HOME}/server/target/classes"
fi


if [ "x$SPARK_HOME" != "x" ] && [ -d "${SPARK_HOME}" ]; then
    addJarInDir "${SPARK_HOME}"
fi

if [ "x$HADOOP_HOME" != "x" ] && [ -d "${HADOOP_HOME}" ]; then
    addJarInDir "${HADOOP_HOME}"
fi

export EXPLORER_CLASSPATH
#export SPARK_CLASSPATH+=${EXPLORER_CLASSPATH} //TODO check if there is any problem not adding explorer classes to
# spark
export CLASSPATH+=${EXPLORER_CLASSPATH}

# Text encoding for
# read/write job into files,
# receiving/displaying query/result.
if [ "x$EXPLORER_ENCODING" = "x" ]; then
  export EXPLORER_ENCODING="UTF-8"
fi

if [ "x$EXPLORER_MEM" = "x" ]; then
  export EXPLORER_MEM="-Xmx2048m -XX:MaxPermSize=1024m"
fi


JAVA_OPTS+="$EXPLORER_JAVA_OPTS -Dfile.encoding=${EXPLORER_ENCODING} ${EXPLORER_MEM}"
export JAVA_OPTS

if [ -n "$JAVA_HOME" ]; then
    EXPLORER_RUNNER="${JAVA_HOME}/bin/java"
else
    EXPLORER_RUNNER=java
fi

export RUNNER


if [ "x$EXPLORER_IDENT_STRING" == "x" ]; then
  export EXPLORER_IDENT_STRING="$USER"
fi

EXPLORER_LOGFILE=$EXPLORER_LOG_DIR/explorer-$EXPLORER_IDENT_STRING-$HOSTNAME.log

if [ -d "$INSTALL_HOME" ]; then
    EXPLORER_LOGFILE="/var/log/sds/explorer/explorer_app.log"
fi

if [ "x$DEBUG" == "x" ] ; then
    export DEBUG=0
fi


if (LOG="TRUE") then
	echo EXPLORER_HOME:            ${EXPLORER_HOME}
	echo EXPLORER_CONF_DIR:        ${EXPLORER_CONF_DIR}
	echo EXPLORER_LOG_DIR:         ${EXPLORER_LOG_DIR}
	echo EXPLORER_PID_DIR:         ${EXPLORER_PID_DIR}
	echo EXPLORER_WAR:             ${EXPLORER_WAR}
	echo EXPLORER_API_WAR:         ${EXPLORER_API_WAR}
	echo EXPLORER_INTERPRETER_DIR: ${EXPLORER_INTERPRETER_DIR}
	echo EXPLORER_CLASSPATH:       ${EXPLORER_CLASSPATH}
    echo EXPLORER_ENCODING:        ${EXPLORER_ENCODING}
    echo EXPLORER_MEM:             ${EXPLORER_MEM}
fi
