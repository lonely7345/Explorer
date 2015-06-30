#!/bin/bash


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

# Validate java installation
function checkJava() {
    if [ -x "$JAVA_HOME/bin/java" ]; then
        JAVA="$JAVA_HOME/bin/java"
    else
        JAVA=`which java`
    fi

    if [ ! -x "$JAVA" ]; then
        echo "Could not find any executable java binary. Please install java in your PATH or set JAVA_HOME" | tee -a $LOG_FILE
        exit 1
    fi
}

# Validate only for root user
function checkUser () {
    if [ $(id -u) -ne 0 ]; then
        echo "You need root privileges to run this script"
        exit 1
    fi
}

# Check run directory
function checkRunDirectory() {
    # check ownership
    # groupship -> stratio
    if [ ! -d "$RUN_DIR" ]; then
        mkdir -p "$RUN_DIR"
        chown "$USER":"$GROUP" $RUN_DIR
        chmod 775 $RUN_DIR
        if [ $? -ne 0 ]; then
            echo "Oops! something unexpected occurred" >&2
            exit 1
        fi
    fi
}

###########################################################################################

# Validate service
function validateService() {
    checkUser
    checkRunDirectory
}

# Validate start
function validateStart() {
    checkJava
}

# Validate stop
function validateStop(){
    return 0
}

# Validate restart
function validateRestart(){
    return 0
}