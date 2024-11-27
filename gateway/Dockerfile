FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app

COPY gateway /app/gateway
WORKDIR /app/gateway
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/gateway/target/*.jar app.jar
EXPOSE 8080
COPY .env /app/.env

ENTRYPOINT ["java", "-jar", "app.jar"]