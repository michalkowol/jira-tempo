# Stage 1: Build
FROM gradle:9.1.0-jdk21-alpine AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --no-daemon --configuration runtimeClasspath || true

COPY src ./src
RUN gradle bootJar --no-daemon

# Stage 2: Extract Spring Boot layers
FROM eclipse-temurin:21-jre-alpine AS layers
WORKDIR /builder
COPY --from=builder /app/build/libs/app.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# Stage 3: Runtime
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /application

COPY --from=layers /builder/extracted/dependencies/ ./
COPY --from=layers /builder/extracted/spring-boot-loader/ ./
COPY --from=layers /builder/extracted/snapshot-dependencies/ ./
COPY --from=layers /builder/extracted/application/ ./

USER app
EXPOSE 8080

ENV JDK_JAVA_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8"

CMD ["sh", "-c", "exec java -jar application.jar --server.port=${PORT:-8080}"]
