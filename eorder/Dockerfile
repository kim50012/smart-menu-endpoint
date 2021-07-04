FROM  base-env-0.0.7:latest
MAINTAINER Orderking


# charset
#RUN apt-get install -y locales
#RUN locale-gen vi_VN.UTF-8
#ENV LANG vi_VN.UTF-8
#ENV LANGUAGE vi_VN:vi
#ENV LC_ALL vi_VN.UTF-8


RUN echo $JAVA_HOME
RUN env

RUN mkdir -p /usr/local/eorder
WORKDIR /usr/local/eorder


#add all files
ADD ./src ./src
ADD ./pom.xml ./pom.xml

RUN echo $USER_HOME_DIR


RUN pwd
RUN ls


#RUN mvn clean
#RUN mvn package
RUN mvn package -Dmaven.test.skip=true

RUN chmod +x /usr/local/eorder/target/run.sh
WORKDIR /usr/local/eorder/target



#ENTRYPOINT ["./app"]
#ENTRYPOINT ["java","-jar","eorder-web-sys-0.1-dev.jar"]

#useage  docker run -it -e JAVA_OPTS='-server -Xms2048m -Xmx2048m -XX:+UseParallelGC'  d4aaa2845a9e --server.port=9000

ENTRYPOINT ["/usr/local/eorder/target/run.sh"]

