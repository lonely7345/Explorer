#!/bin/bash
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

EXPLORER_SERVER=com.stratio.notebook.server.ZeppelinServer
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

