FROM eclipse-temurin:21-alpine
COPY build/libs/app.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]
