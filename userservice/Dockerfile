# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

COPY AnimalsDataCore /app/AnimalsDataCore
WORKDIR /app/AnimalsDataCore
RUN mvn dependency:go-offline -B
RUN mvn clean install

WORKDIR /app/userservice
COPY userservice/pom.xml /app/userservice/pom.xml
RUN mvn dependency:go-offline -B

COPY userservice/src /app/userservice/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y wget

WORKDIR /app

COPY --from=builder /app/userservice/target/*.jar /app/userservice.jar

## Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "userservice.jar"]