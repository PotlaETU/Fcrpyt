FROM eclipse-temurin:22-jre-alpine

LABEL authors="antho"

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]
