# Stage 1: Build
FROM gradle:9.1.0-jdk21-alpine AS builder
COPY . /app
WORKDIR /app
RUN gradle assemble --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-alpine
COPY --from=builder /app/build/libs/app.jar /app.jar
EXPOSE 8080
EXPOSE 10000
CMD ["sh", "-c", "exec java -Dserver.port=${PORT:-8080} -jar /app.jar"]
