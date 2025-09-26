# Stage 1: Build
FROM eclipse-temurin:17-alpine AS builder
COPY . /app
WORKDIR /app
RUN ./gradlew assemble --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-alpine
COPY --from=builder /app/build/libs/app.jar /app.jar
EXPOSE 8080
CMD ["sh", "-c", "exec java -Dserver.port=${PORT:-8080} -jar /app.jar"]
