# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Build AnimalsDataCore
WORKDIR /app/AnimalsDataCore
COPY AnimalsDataCore/pom.xml /app/AnimalsDataCore/pom.xml
RUN mvn dependency:go-offline -B
COPY AnimalsDataCore/src /app/AnimalsDataCore/src
RUN mvn clean install

# Build reportservice
WORKDIR /app/reportservice

COPY reportservice/pom.xml /app/reportservice/pom.xml
RUN mvn dependency:go-offline -B

COPY reportservice/src /app/reportservice/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim AS production
RUN apt-get update && apt-get install -y wget

WORKDIR /app

COPY --from=builder /app/reportservice/target/*.jar report-service.jar

## Run the Spring Boot application
CMD ["java", "-jar", "report-service.jar"]