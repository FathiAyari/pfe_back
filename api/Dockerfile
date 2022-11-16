FROM openjdk:17-slim
WORKDIR /app
ARG JAR_FILE=build/libs/api.jar
COPY ${JAR_FILE} app.jar
LABEL app=api
ENTRYPOINT [ "java", "-jar", "app.jar" ]