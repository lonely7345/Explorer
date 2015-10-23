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

function usage() {
  echo "Usage: bin/explorer.sh"
  exit 0
}

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin">/dev/null; pwd`

. $bin/common.sh

HOSTNAME=`hostname`
EXPLORER_LOGFILE=$EXPLORER_LOG_DIR/explorer-$EXPLORER_IDENT_STRING-$HOSTNAME.log
echo EXPLORER_HOME $EXPLORER_HOME
echo EXPLORER_LOGFILE $EXPLORER_LOG_DIR/explorer-$EXPLORER_IDENT_STRING-$HOSTNAME.log

EXPLORER_SERVER=com.stratio.explorer.server.ExplorerServer
JAVA_OPTS+=" -Dnexplorer.log.file=$EXPLORER_LOGFILE"

if [[ ! -d "$EXPLORER_LOG_DIR" ]]; then
  echo "Log dir doesn't exist, create $EXPLORER_LOG_DIR"
  mkdir -p "$EXPLORER_LOG_DIR"
fi

if [[ ! -d "$EXPLORER_PID_DIR" ]]; then
  echo "Pid dir doesn't exist, create $EXPLORER_PID_DIR"
  mkdir -p "$EXPLORER_PID_DIR"
fi

if [[ ! -d "$EXPLORER_PID_DIR" ]]; then
  echo "Pid dir doesn't exist, create $EXPLORER_PID_DIR"
  mkdir -p "$EXPLORER_PID_DIR"
fi


exec $EXPLORER_RUNNER $JAVA_OPTS -cp $CLASSPATH $EXPLORER_SERVER "$@"

