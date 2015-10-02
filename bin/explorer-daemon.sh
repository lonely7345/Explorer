#!/bin/bash
#
# Licensed to STRATIO (C) under one or more contributor license agreements.
# See the NOTICE file distributed with this work for additional information
# regarding copyright ownership.  The STRATIO (C) licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

usage="Usage: explorer-daemon.sh [--config <conf-dir>]\
 (start|stop|restart|status) \
 <args...>"

# if no args specified, show usage
if [ $# -le 0 ]; then
  echo $usage
  exit 1
fi

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin">/dev/null; pwd`

. $bin/common.sh


# get arguments
startStop=$1
shift
args=$1
shift



HOSTNAME=`hostname`
EXPLORER_LOGFILE=$EXPLORER_LOG_DIR/explorer-$EXPLORER_IDENT_STRING-$HOSTNAME.log
log=$EXPLORER_LOG_DIR/explorer-$EXPORER_IDENT_STRING-$HOSTNAME.out
pid=$EXPLORER_PID_DIR/explorer-$EXPLORER_IDENT_STRING-$HOSTNAME.pid


if [ "${EXPLORER_NICENESS}" = "" ]; then
    export EXPLORER_NICENESS=0
fi

EXPLORER_MAIN=com.stratio.notebook.server.ZeppelinServer

JAVA_OPTS+=" -Dnotebook.log.file=$EXPLORER_LOGFILE"

function init(){
    if [ ! -d "$EXPLORER_LOG_DIR" ]; then
	echo "Log dir doesn't exist, create $EXPLORER_LOG_DIR"
	mkdir -p "$EXPLORER_LOG_DIR"
    fi

    if [ ! -d "$EXPLORER_PID_DIR" ]; then
	echo "Pid dir doesn't exist, create $EXPLORER_PID_DIR"
	mkdir -p "$EXPLORER_PID_DIR"
    fi

	if [ ! -d "$EXPLORER_NOTEBOOK_DIR" ]; then
		echo "Notebooks dir doesn't exist, create $EXPLORER_NOTEBOOK_DIR"
		mkdir -p "$EXPLORER_NOTEBOOK_DIR"
	fi

}

function start(){
    if [ -f "$pid" ]; then
	if kill -0 `cat $pid` > /dev/null 2>&1; then
	    echo explorer running as process `cat $pid`. Stop it first.
	    exit 1
	fi
    fi
    
    init

    echo "Start Explorer"
    nohup nice -n $EXPLORER_NICENESS $EXPLORER_RUNNER $JAVA_OPTS -cp $CLASSPATH $EXPLORER_MAIN "$@" >> "$log" 2>&1 < /dev/null &
    newpid=$!
    echo $newpid > $pid
    sleep 2
    # Check if the process has died; in that case we'll tail the log so the user can see
    if ! kill -0 $newpid >/dev/null 2>&1; then
	echo "failed to launch Explorer:"
	if [ "${CI}" == "true" ]; then
	
	    tail -1000 "$log" | sed 's/^/  /'
	else
	    tail -5 "$log" | sed 's/^/  /'
	fi
	echo "full log in $log"
    fi

    if [ "${CI}" == "true" ]; then  # if it is CI wait until server is up and running and ready to serve.
	COUNT=0
	while [ $COUNT -lt 30 ]; do
	    curl -v localhost:8084 2>&1 | grep '200 OK'
	    if [ $? -ne 0 ]; then
		sleep 1
		continue
	    else
		break
	    fi
	    let "COUNT+=1"
	done
    fi

}

function stop(){
    if [ -f $pid ]; then
	if kill -0 `cat $pid` > /dev/null 2>&1; then
	    echo Shutdown Explorer

	    COUNT=0
	    while [ $COUNT -lt 5 ]; do
		kill `cat $pid` > /dev/null 2> /dev/null
		if kill -0 `cat $pid` > /dev/null 2>&1; then
		    sleep 3
		    let "COUNT+=1"
		else
		    break
		fi
	    done
	    if kill -0 `cat $pid` > /dev/null 2>&1; then
		echo "failed to stop Explorer:"
		tail -2 "$log" | sed 's/^/  /'
		echo "full log in $log"
	    fi

	else
	    echo no Explorer to stop
	fi
    else
	echo no Explorer to stop
    fi

}

function status(){
    if [ -f "${pid}" ] && kill -0 `cat $pid` > /dev/null 2>&1; then
	echo "Explorer is running `cat $pid`"
	exit 0
    else
	echo "Explorer is not running"
	exit 1
    fi
}


case $startStop in
    (start)
	start
	;;
    (stop)
	stop
	;;
    (restart)
	stop
	start
	;;
    (status)
	status
	;;
    (*)
	echo $usage
	exit 1
	;;
esac
