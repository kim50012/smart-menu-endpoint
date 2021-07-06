#!/bin/bash

echo "Current Path:"$PWD

#JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:+UseParallelGC"

a=" -jar eorder-web-sys-0.1-dev.jar"


r_java_opts=$JAVA_OPTS" "$a

echo ${r_java_opts}

exec  java $r_java_opts  "$@"