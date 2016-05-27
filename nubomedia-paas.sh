#!/bin/bash

source ./gradle.properties

_version=${version}

_base="/opt/nubomedia/nubomedia-paas"
_process_name="nubomedia-paas"
_screen_name="nubomedia"

function checkBinary {
  if command -v $1 >/dev/null 2>&1; then
     return 0
   else
     echo >&2 "FAILED."
     return 1
   fi
}

_ex='sh -c'
if [ "$_user" != 'root' ]; then
    if checkBinary sudo; then
        _ex='sudo -E sh -c'
    elif checkBinary su; then
        _ex='su -c'
    fi
fi


function check_already_running {
    pgrep -f ${_process_name}-${_version}.jar
    if [ "$?" -eq "0" ]; then
        echo "${_process_name} is already running.."
        exit;
    fi
}

function start_checks {
    check_already_running
    if [ ! -d build/  ]
        then
            compile
    fi
}

function start {
    start_checks
    screen_exists=$(screen -ls | grep ${_screen_name} | wc -l);
    if [ "${screen_exists}" -eq 0 ]; then
        screen -c screenrc -d -m -S ${_screen_name} -t ${_process_name} java -jar "$_base/build/libs/$_process_name-$_version.jar"
    else
        screen -S $_screen_name -p 0 -X screen -t $_process_name java -jar "$_base/build/libs/$_process_name-$_version.jar"
    fi
}

function start_fg {
    start_checks
    java -jar "build/libs/${_process_name}-$_version.jar" --spring.config.location=file:${_openbaton_config_file}
}


function stop {
    if screen -list | grep ${_screen_name}; then
	    screen -S ${_screen_name} -p 0 -X stuff "exit$(printf \\r)"
    fi
}

function restart {
    kill
    start
}


function kill {
    pkill -f ${_process_name}-${_version}.jar
}


function compile {
    ./gradlew build -x test 
}

function tests {
    ./gradlew test
}

function clean {
    ./gradlew clean
}

function end {
    exit
}
function usage {
    echo -e "Open-Baton\n"
    echo -e "Usage:\n\t ./openbaton.sh [compile|install_plugins|start|start_fg|stop|test|kill|clean]"
}

##
#   MAIN
##

if [ $# -eq 0 ]
   then
        usage
        exit 1
fi

declare -a cmds=($@)
for (( i = 0; i <  ${#cmds[*]}; ++ i ))
do
    case ${cmds[$i]} in
        "clean" )
            clean ;;
        "install_plugins" )
            install_plugins ;;
        "sc" )
            clean
            compile
            start ;;
        "start" )
            start ;;
        "start_fg" )
            start_fg ;;
        "stop" )
            stop ;;
        "restart" )
            restart ;;
        "compile" )
            compile ;;
        "kill" )
            kill ;;
        "test" )
            tests ;;
        * )
            usage
            end ;;
    esac
    if [[ $? -ne 0 ]]; 
    then
	    exit 1
    fi
done

