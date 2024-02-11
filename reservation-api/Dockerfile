FROM gradle:8.6-jdk17-alpine as builder
WORKDIR /builder
COPY . /builder/.
RUN gradle clean bootJar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /builder/build/libs/*.jar /app/reservation-api.jar
ENTRYPOINT ["java", "-jar", "/app/reservation-api.jar"]