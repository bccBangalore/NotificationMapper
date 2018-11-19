FROM java:8
VOLUME /tmp
ARG JAR_FILE=target/notificationWrapper-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} notificationWrapper.jar
RUN bash -c 'touch /notificationWrapper.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/notificationWrapper.jar"]