#!/bin/sh

##############################################################################
#
# Gradle start up script for POSIX systems
#
##############################################################################

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

# Function to setup the environment
setup_environment() {
    APP_HOME=$(cd "$(dirname "$0")" && pwd)
    APP_NAME="Gradle"
    APP_BASE_NAME=$(basename "$0")

    # Add default JVM options here.
    DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

    # Determine the Java command to use to start the JVM.
    if [ -n "$JAVA_HOME" ] ; then
        if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
            JAVACMD=$JAVA_HOME/jre/sh/java
        else
            JAVACMD=$JAVA_HOME/bin/java
        fi
        if [ ! -x "$JAVACMD" ] ; then
            die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME
Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
        fi
    else
        JAVACMD=java
        which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi

    # Increase the maximum file descriptors if we can.
    if [ "$MAX_FD" != "maximum" ] && ! command -v ulimit >/dev/null 2>&1; then
        echo "ulimit not found, skipping max fd adjustment"
    elif [ "$MAX_FD" != "maximum" ]; then
        ulimit -n "$MAX_FD" 2>/dev/null || echo "Could not set maximum file descriptor limit to $MAX_FD"
    fi

    # Collect all arguments for the java command, stacking in reverse order:
    #   * args from the command line
    #   * the main class name
    #   * -classpath
    #   * -D...appname settings
    #   * --module-path (if needed)
    #   * DEFAULT_JVM_OPTS, JAVA_OPTS, and GRADLE_OPTS environment variables.

    # For Cygwin or MSYS, switch paths to Windows format before running java
    if [ "$(uname -o 2>/dev/null)" = "Cygwin" ] || [ "${MSYSTEM-}" = "MINGW64" ] || [ "${MSYSTEM-}" = "MINGW32" ]; then
        APP_HOME=$(cygpath --path --mixed "$APP_HOME")
        CLASSPATH=$(cygpath --path --mixed "$APP_HOME/gradle/wrapper/gradle-wrapper.jar")
        JAVACMD=$(cygpath --unix "$JAVACMD")
        # Now convert the arguments - kludge to limit ourselves to /bin/sh
        for arg do
            if
                case $arg in                                #(
                  -*)   false ;;                            # don't mess with options #(
                  /?*)  t=${arg#/} t=/${t%%/*}              # looks like a POSIX filepath
                        [ -e "$t" ] ;;                      #(
                  *)    false ;;
                esac
            then
                arg=$(cygpath --path --ignore --mixed "$arg")
            fi
            # Roll the args list around exactly as many times as the number of
            # args, so each arg winds up back at the beginning but in a different order
            args="$arg $args"
        done
        # Add build-essentials to the start of the args if needed
        args="--build-essentials $args"
    else
        CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
    fi

    # Collect all arguments for the java command;
    #   * $DEFAULT_JVM_OPTS, $JAVA_OPTS, and $GRADLE_OPTS can contain fragments of
    #     shell script including quotes and variable substitutions, so put them in
    #     double quotes to make sure that they get re-expanded; and
    #   * put everything else in single quotes, so that it is not re-expanded.
    set -- \
        "-Dorg.gradle.appname=$APP_BASE_NAME" \
        -classpath "$CLASSPATH" \
        org.gradle.wrapper.GradleWrapperMain \
        "$@"

    # Use "xargs" to parse quoted args.
    # With -n1 it outputs one arg per line, with the parts in the same order.
    # With -L1 it outputs one arg per line, but tries to keep whole lines.
    # This is combined to work around the fact that "set --" preserves spaces but
    # not newlines, and we want to preserve original quoting.
    xargs_output=$(printf "%s\0" "$@" | xargs -0 -n1 -L1 printf "%s\n")
    set -- $xargs_output

    # Collect the arguments as the original quoted strings
    eval set -- "$(printf "%q " "$@")"

    # Add DEFAULT_JVM_OPTS, JAVA_OPTS, and GRADLE_OPTS
    if [ -n "$DEFAULT_JVM_OPTS" ]; then
        set -- "$DEFAULT_JVM_OPTS" ${1+"$@"}
    fi
    if [ -n "$JAVA_OPTS" ]; then
        set -- "$JAVA_OPTS" ${1+"$@"}
    fi
    if [ -n "$GRADLE_OPTS" ]; then
        set -- "$GRADLE_OPTS" ${1+"$@"}
    fi

    eval "set -- $(
        printf '%s\n' "$DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS" |
        xargs -n1 |
        sed ' s~[^-[:alnum:]+,./:=@_]~\\&~g; ' |
        tr '\n' ' '
        )" '"$@"'

    exec "$JAVACMD" "$@"
}

die() {
    echo "$*" >&2
    exit 1
}

setup_environment
