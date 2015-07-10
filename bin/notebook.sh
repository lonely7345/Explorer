#!/bin/bash
#
# Copyright 2007 The Apache Software Foundation
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Run Notebook
#
function usage() {
  echo "Usage: bin/notebook.sh"
  exit 0
}

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin">/dev/null; pwd`

. $bin/common.sh

HOSTNAME=`hostname`
NOTEBOOK_LOGFILE=$NOTEBOOK_LOG_DIR/notebook-$NOTEBOOK_IDENT_STRING-$HOSTNAME.log
echo NOTEBOOK_HOME $NOTEBOOK_HOME
echo NOTEBOOK_LOGFILE $NOTEBOOK_LOG_DIR/notebook-$NOTEBOOK_IDENT_STRING-$HOSTNAME.log

NOTEBOOK_SERVER=com.nflabs.zeppelin.server.ZeppelinServer
JAVA_OPTS+=" -Dnotebook.log.file=$NOTEBOOK_LOGFILE"

if [[ ! -d "$NOTEBOOK_LOG_DIR" ]]; then
  echo "Log dir doesn't exist, create $NOTEBOOK_LOG_DIR"
  mkdir -p "$NOTEBOOK_LOG_DIR"
fi

if [[ ! -d "$NOTEBOOK_PID_DIR" ]]; then
  echo "Pid dir doesn't exist, create $NOTEBOOK_PID_DIR"
  mkdir -p "$NOTEBOOK_PID_DIR"
fi

if [[ ! -d "$NOTEBOOK_NOTEBOOK_DIR" ]]; then
  echo "Pid dir doesn't exist, create $NOTEBOOK_NOTEBOOK_DIR"
  mkdir -p "$NOTEBOOK_NOTEBOOK_DIR"
fi

#if [ "x$SPARK_HOME" != "x" ]; then
#  source $SPARK_HOME/bin/utils.sh
#  SUBMIT_USAGE_FUNCTION=usage
#  gatherSparkSubmitOpts "$@"
#  NOTEBOOK_RUNNER=$SPARK_HOME/bin/spark-submit

#  exec $NOTEBOOK_NICENESS $NOTEBOOK_RUNNER --class $NOTEBOOK_SERVER "${SUBMISSION_OPTS[@]}" --driver-java-options
# -Dnotebook.log.file=$NOTEBOOK_LOGFILE spark-shell "${APPLICATION_OPTS[@]}"
#else
  exec $NOTEBOOK_RUNNER $JAVA_OPTS -cp $CLASSPATH $NOTEBOOK_SERVER "$@"
#fi
