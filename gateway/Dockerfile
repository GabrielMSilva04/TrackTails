# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app/gateway

COPY gateway/pom.xml /app/gateway/pom.xml
RUN mvn dependency:go-offline -B

COPY gateway/src /app/gateway/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y wget

WORKDIR /app

COPY --from=builder /app/gateway/target/*.jar app/gateway-service.jar
EXPOSE 80

ENTRYPOINT ["java", "-jar", "app/gateway-service.jar"]