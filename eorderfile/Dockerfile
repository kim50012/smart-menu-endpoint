FROM  base-env-0.0.7:latest
MAINTAINER Orderking

# charset
#RUN apt-get install -y locales
#RUN locale-gen zh_CN.UTF-8
#ENV LANG zh_CN.UTF-8
#ENV LANGUAGE zh_CN:zh
#ENV LC_ALL zh_CN.UTF-8


RUN echo $JAVA_HOME
RUN env

RUN mkdir -p /usr/local/basoft-file-app
WORKDIR /usr/local/basoft-file-app


#add all files
ADD ./src ./src
ADD ./pom.xml ./pom.xml

RUN echo $USER_HOME_DIR


RUN pwd
RUN ls


#RUN mvn clean
#RUN mvn package
RUN mvn package -Dmaven.test.skip=true

RUN chmod +x /usr/local/basoft-file-app/target/run.sh
WORKDIR /usr/local/basoft-file-app/target


ENTRYPOINT ["/usr/local/basoft-file-app/target/run.sh"]

