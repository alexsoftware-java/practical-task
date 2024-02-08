FROM gradle:8.6-jdk17-alpine as builder
WORKDIR /app
COPY . /app/.
RUN gradle bootJar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/reservation-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/reservation-api.jar"]