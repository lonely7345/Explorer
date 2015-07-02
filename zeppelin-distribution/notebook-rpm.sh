#!/bin/bash
#
#   /etc/rc.d/init.d/elasticsearch
#
#   Elasticsearch service
#
### BEGIN INIT INFO
# Provides:          Notebook
# Required-Start:    $network $remote_fs $named
# Required-Stop:     $network $remote_fs $named
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Starts notebook
# Description:       Starts notebook using start-stop-daemon
### END INIT INFO

# Source function library.
. /etc/init.d/functions

# Only configuration needed here
NAME="notebook"
DESC="Notebook extended shell"

PATH=/bin:/usr/bin:/sbin:/usr/sbin
VARIABLES="/etc/default/$NAME-variables"
FUNCTIONS="/etc/default/$NAME-functions"

# Source function library.
. /etc/rc.d/init.d/functions

# Source default configuration
if [ -r $VARIABLES ]; then
    . $VARIABLES
fi

# Source check functions & validate service configuration
. $FUNCTIONS && validateService

start() {
    validateStart
    echo -n "Starting $NAME: "
    #daemon --user $USER --pidfile $NOTEBOOK_PID_FILE "$DAEMON"
    daemon --user $USER --pidfile $NOTEBOOK_PID_FILE "$DAEMON"
    #$NOTEBOOK_RUNNER $JAVA_OPTS -cp $CLASSPATH $NOTEBOOK_SERVER "$@" >> "$log" 2>&1 < /dev/null &
    echo
    touch $LOCKFILE
}

stop() {
    validateStop
    echo -n "Shutting down $NAME: "
    killproc -p $NOTEBOOK_PID_FILE $NAME
    local exit_status=$?
    echo
    rm -f $LOCKFILE
    return $exit_status
}

restart(){
    validateRestart
    stop
    sleep 1
    start
}

condrestart(){
    if [ -f /var/lock/subsys/$NAME ]; then
        restart || { echo "won't restart: $NAME is stopped" ; exit 1 ; }
    fi
}

getStatus(){
    status -p $NOTEBOOK_PID_FILE $NAME -l $LOCKFILE
}

usage(){
    echo "Usage: $NAME {start|stop|status|restart|reload|force-reload|condrestart}"
    exit 1
}

case "$1" in
    start)                          start ;;
    stop)                           stop ;;
    status)                         getStatus ;;
    restart|reload|force-reload)    restart ;;
    condrestart)                    condrestart ;;
    *)                              usage ;;
esac
exit $?