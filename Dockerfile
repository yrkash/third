FROM openjdk:17-oracle

WORKDIR /app

EXPOSE 8080

COPY target/*.jar app.jar

ENTRYPOINT [ "java","-jar","app.jar" ]