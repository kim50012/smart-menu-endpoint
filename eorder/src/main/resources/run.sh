#!/bin/bash

echo "Current Path:"$PWD

#JAVA_OPTS="-server -Xms2048m -Xmx2048m -XX:+UseParallelGC"

a=" -jar eorder-web-sys-0.1-dev.jar"


r_java_opts=$JAVA_OPTS" "$a

echo ${r_java_opts}

exec  java $r_java_opts  "$@"