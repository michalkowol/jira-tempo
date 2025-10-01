# Stage 1: Build
FROM gradle:9.1.0-jdk21-alpine AS builder
COPY . /app
WORKDIR /app
RUN gradle assemble --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-alpine
COPY --from=builder /app/build/libs/app.jar /app.jar
EXPOSE 8080
EXPOSE $PORT
ENV JDK_JAVA_OPTIONS="-XX:+UseG1GC"
CMD ["sh", "-c", "SERVER_PORT=${PORT:-8080} && exec java -jar /app.jar"]
