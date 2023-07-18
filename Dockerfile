FROM openjdk:18
EXPOSE 8080
EXPOSE 5432
ARG JAR_FILE=out/artifacts/demo_jar/demo.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]