#!/bin/bash

echo "Current Path:"$PWD

#JAVA_OPTS="-server -Xms2048m -Xmx2048m -XX:+UseParallelGC"

a=" -jar cloud-upload-file-app-1.0.jar"


r_java_opts=$JAVA_OPTS" "$a

echo ${r_java_opts}

exec  java $r_java_opts  "$@"