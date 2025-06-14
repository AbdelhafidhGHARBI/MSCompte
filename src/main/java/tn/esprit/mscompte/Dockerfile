FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar mscompte.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "mscompte.jar"]
