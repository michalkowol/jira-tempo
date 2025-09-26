# Stage 1: Build
FROM eclipse-temurin:21-alpine AS builder
RUN ./gradlew assemble

# Stage 2: Runtime
FROM eclipse-temurin:21-alpine
COPY --from=builder /app/build/libs/app.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]
