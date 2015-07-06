#! /bin/bash
### BEGIN INIT INFO
# Provides:          notebook
# Required-Start:    $network $remote_fs $syslog
# Required-Stop:     $network $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Starts notebook
# Description:       Notebook Server
### END INIT INFO


# Only configuration needed here
NAME="notebook"
DESC="Notebook extended shell"

# PATH should only include /usr/* if it runs after the mountnfs.sh script
PATH=/sbin:/usr/sbin:/bin:/usr/bin
VARIABLES="/etc/default/$NAME-variables"
FUNCTIONS="/etc/default/$NAME-functions"
DEFAULT="/etc/default/$NAME"

# Read default configuration variable file if it is present
[ -r $DEFAULT ] && . $DEFAULT

# Load the VERBOSE setting and other rcS variables
. /lib/init/vars.sh

# Define LSB log_* functions.
# Depend on lsb-base (>= 3.2-14) to ensure that this file is present
# and status_of_proc is working.
. /lib/lsb/init-functions

# Source service configuration
if [ -r $VARIABLES ]; then
    . $VARIABLES
fi

# Source check functions & validate service configuration
. $FUNCTIONS && validateService

#
# Function that starts the daemon/service
#
do_start(){
	# Return
	#   0 if daemon has been started
	#   1 if daemon was already running
	#   2 if daemon could not be started
    if lockfile-check $LOCKFILE; then # Do not start if lockfile found
    	return 1
    fi
    lockfile-create $LOCKFILE
    start-stop-daemon --start --user $USER -c $USER --pidfile $PIDFILE --exec $DAEMON --test > /dev/null || return 1
    start-stop-daemon --start --user $USER -c $USER -b --quiet --pidfile $PIDFILE --exec $DAEMON -- $DAEMON_OPTS || return 2
	# Add code here, if necessary, that waits for the process to be ready
	# to handle requests from services started subsequently which depend
	# on this one.  As a last resort, sleep for some time.
}

#
# Function that stops the daemon/service
#
do_stop(){
    validateStop
    # Return
    #   0 if daemon has been stopped
    #   1 if daemon was already stopped
    #   2 if daemon could not be stopped
    #   other if a failure occurred
    RETVAL=0
    if [ -f "$PIDFILE" ]; then
        start-stop-daemon --stop --pidfile "$PIDFILE" --user "$USER" --quiet --retry=TERM/20/KILL/5 #--name $NAME
        RETVAL="$?"
        if [ "$RETVAL" -eq 1 ]; then
                log_progress_msg "$DESC is not running but pid file exists, cleaning up"
        elif [ "$RETVAL" -eq 3 ]; then
                PID="$(cat $PIDFILE)"
                log_failure_msg "Failed to stop $DESC (pid $PID)"
                exit 1
        fi
        rm -f "$PIDFILE"
        lockfile-remove $LOCKFILE
    else
        log_progress_msg "(not running)"
    fi
    [ "$RETVAL" = 2 ] && return 2

    # Wait for children to finish too if this is a daemon that forks
    # and if the daemon is only ever run from this initscript.
    # If the above conditions are not satisfied then add some other code
    # that waits for the process to drop all resources that could be
    # needed by services started subsequently.  A last resort is to
    # sleep for some time.
    #start-stop-daemon --stop --quiet --oknodo --retry=0/20/KILL/5 --exec $DAEMON
    #[ "$?" = 2 ] && return 2

    # Many daemons don't delete their pidfiles when they exit.
    rm -f "$PIDFILE"
    return "$RETVAL" 
}

#
# Function that sends a SIGHUP to the daemon/service
#
#do_reload() {
#    #
#    # If the daemon can reload its configuration without
#    # restarting (for example, when it is sent a SIGHUP),
#    # then implement that here.
#    #
#    #start-stop-daemon --stop --signal 1 --quiet --pidfile $PIDFILE --name $NAME
#}

case "$1" in
  start)
    log_daemon_msg "Starting $DESC" "$NAME"
    do_start
    case "$?" in
        0|1) log_end_msg 0 ;;
        2) log_end_msg 1 ;;
    esac
    ;;
  stop)
    log_daemon_msg "Stopping $DESC" "$NAME"
    do_stop
    case "$?" in
        0|1) log_end_msg 0 ;;
        2) log_end_msg 1 ;;
    esac
    ;;
  status)
    status_of_proc -p "$PIDFILE" "$DAEMON" "$NAME" && exit 0 || exit $?
    ;;
  #reload|force-reload)
	#
	# If do_reload() is not implemented then leave this commented out
	# and leave 'force-reload' as an alias for 'restart'.
	#
	#log_daemon_msg "Reloading $DESC" "$NAME"
	#do_reload
	#log_end_msg $?
	#;;
  restart|force-reload)
    #
    # If the "reload" option is implemented then remove the
    # 'force-reload' alias
    #
    validateRestart
    log_daemon_msg "Restarting $DESC" "$NAME"
    do_stop
    case "$?" in
      0|1)
        do_start
        case "$?" in
            0) log_end_msg 0 ;;
            1) log_end_msg 1 ;; # Old process is still running
            *) log_end_msg 1 ;; # Failed to start
        esac
        ;;
      *)
        # Failed to stop
        log_end_msg 1
        ;;
    esac
    ;;
  *)
    #echo "Usage: $SCRIPTNAME {start|stop|restart|reload|force-reload}" >&2
    echo "Usage: $SCRIPTNAME {start|stop|status|restart|force-reload}" >&2
    exit 3
    ;;
esac

exit 0