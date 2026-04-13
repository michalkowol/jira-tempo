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
COPY --from=builder /app/build/libs/app.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

# Stage 3: Runtime
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /application

COPY --from=layers --chown=app:app /builder/extracted/dependencies/ ./
COPY --from=layers --chown=app:app /builder/extracted/spring-boot-loader/ ./
COPY --from=layers --chown=app:app /builder/extracted/snapshot-dependencies/ ./
COPY --from=layers --chown=app:app /builder/extracted/application/ ./

USER app

ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=75.0"
ENV SERVER_PORT=8080
ENTRYPOINT ["java", "-jar", "app.jar"]
