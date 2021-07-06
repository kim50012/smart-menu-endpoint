FROM  base-env-0.0.7:latest
MAINTAINER Orderking

## charset
#RUN apt-get install -y locales
#RUN locale-gen zh_CN.UTF-8
#ENV LANG zh_CN.UTF-8
#ENV LANGUAGE zh_CN:zh
#ENV LC_ALL zh_CN.UTF-8
RUN echo $JAVA_HOME
RUN env

RUN mkdir -p /usr/local/bawx
WORKDIR /usr/local/bawx

## add all files
ADD ./service-definition/src ./service-definition/src
ADD ./service-definition/pom.xml ./service-definition/pom.xml
ADD ./service-impl/src ./service-impl/src
ADD ./service-impl/pom.xml ./service-impl/pom.xml
ADD ./web-sys/src ./web-sys/src
ADD ./web-sys/pom.xml ./web-sys/pom.xml
ADD ./pom.xml ./pom.xml

RUN echo $USER_HOME_DIR

RUN pwd
RUN ls

RUN mvn install -Dmaven.test.skip=true

WORKDIR /usr/local/bawx/web-sys/target
RUN chmod +x run.sh

## useage  docker run -it -e JAVA_OPTS='-server -Xms2048m -Xmx2048m -XX:+UseParallelGC'  d4aaa2845a9e --server.port=9000
ENTRYPOINT ["/usr/local/bawx/web-sys/target/run.sh"]
