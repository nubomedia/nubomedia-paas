#!/bin/bash

source ./gradle.properties

_version=${version}

_nubomedia_paas_base="/opt/nubomedia/nubomedia-paas"
_nubomedia_config_file="/etc/nubomedia/paas.properties"
#_message_queue_base="apache-activemq-5.11.1"
#_openbaton_config_file=/etc/openbaton/openbaton.properties

function start_mysql_osx {
    sudo /usr/local/mysql/support-files/mysql.server start
}

function start_mysql_linux {
    sudo service mysql start
}

function check_mysql {
    if [[ "$OSTYPE" == "linux-gnu" ]]; then
	result=$(pgrep mysql | wc -l);
        if [ ${result} -eq 0 ]; then
                read -p "mysql is down, would you like to start it ([y]/n):" yn
		case $yn in
			[Yy]* ) start_mysql_linux ; break;;
			[Nn]* ) echo "you can't proceed withuot having mysql up and running"
				exit;;
			* ) start_mysql_linux;;
		esac
        else
                echo "mysql is already running.."
        fi
    elif [[ "$OSTYPE" == "darwin"* ]]; then
	mysqladmin status
	result=$?
        if [ "${result}" -eq "0" ]; then
                echo "mysql service running..."
        else
                read -p "mysql is down, would you like to start it ([y]/n):" yn
                case $yn in
                        [Yy]* ) start_mysql_osx ; break;;
                        [Nn]* ) exit;;
                        * ) start_mysql_osx;;
                esac
        fi
    fi
}

function check_already_running {
        result=$(screen -ls | grep nubomedia-paas | wc -l);
        if [ "${result}" -ne "0" ]; then
                echo "Nubomedia-PaaS is already running.."
		exit;
        fi
}

function start {

    if [ ! -d build/  ]
        then
            compile
    fi

    #check_activemq
    #check_mysql
    #check_already_running
    #if [ 0 -eq $? ]
        #then
	    #screen -X eval "chdir $PWD"
	#screen -c screenrc -d -m -S ms-vnfm -t ms-vnfm java -jar "build/libs/ms-vnfm-$_version.jar"
	pushd "${_nubomedia_paas_base}"
	#                                                                   build/libs/nubomedia-paas-api-0.1-SNAPSHOT.jar
	screen -d -m -S paas-manager -t nubomedia-paas-manager java -jar "/opt/nubomedia/nubomedia-paas/build/libs/nubomedia-paas-api-$_version.jar" --spring.config.location=file:${_nubomedia_config_file}
	    #screen -c screenrc -r -p 0
	popd
    #fi
}

function stop {
    if screen -list | grep "paas-manager"; then
	    screen -S paas-manager -p 0 -X stuff "exit$(printf \\r)"
    fi
}

function restart {
    kill
    start
}


function kill {
    if screen -list | grep "paas-manager"; then
	    screen -ls | grep paas-manager | cut -d. -f1 | awk '{print $1}' | xargs kill
    fi
}

function init {
    if [ ! -f $_nubomedia_config_file ]; then
        if [ $EUID != 0 ]; then
            echo "creating the directory and copying the file"
            sudo -E sh -c "mkdir /etc/nubomedia; cp ${_nubomedia_paas_base}/src/main/resources/paas.properties ${_nubomedia_config_file}"
            #echo "copying the file, insert the administrator password" | sudo -kS cp ${_nubomedia_paas_base}/src/main/resources/paas.properties ${_nubomedia_config_file}
        else
            echo "creating the directory"
            mkdir /etc/nubomedia
            echo "copying the file"
            cp ${_nubomedia_paas_base}/src/main/resources/paas.properties ${_nubomedia_config_file}
        fi
    else
        echo "Properties file already exist"
    fi
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
    echo -e "Nubomedia PaaS API\n"
    echo -e "Usage:\n\t ./nubomedia-paas.sh [compile|start|stop|test|kill|clean]"
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
        "sc" )
            clean
            compile
            start ;;
        "start" )
            start ;;
        "stop" )
            stop ;;
        "init" )
            init ;;
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

