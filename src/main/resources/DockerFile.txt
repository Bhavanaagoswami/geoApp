FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} geoapp.jar
ENTRYPOINT ["java","-jar","/geoapp.jar"]