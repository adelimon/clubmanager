FROM openjdk:8-jdk-alpine
RUN addgroup -S clubmanager && adduser -S clubmanager -G clubmanager
USER clubmanager:clubmanager
ARG JAR_FILE=target/*.war
COPY ${JAR_FILE} app.war
ENTRYPOINT ["java","-jar","/app.war"]