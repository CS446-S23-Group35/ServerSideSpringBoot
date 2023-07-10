FROM openjdk:18
EXPOSE 8080
ARG JAR_FILE=out/artifacts/demo_jar/demo.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]