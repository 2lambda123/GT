FROM openjdk:8-jre-alpine
EXPOSE 8084
WORKDIR /app
COPY target/gt-1.0.0.jar .
ENTRYPOINT [ "java", "-jar", "gt-1.0.0.jar" ]
