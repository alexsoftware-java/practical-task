FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app
COPY . /app/.
CMD ["./gradlew", "clean", "bootJar"]

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/reservation-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/reservation-api.jar"]