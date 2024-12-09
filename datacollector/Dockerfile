# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Build AnimalsDataCore
WORKDIR /app/AnimalsDataCore
COPY AnimalsDataCore/pom.xml /app/AnimalsDataCore/pom.xml
COPY AnimalsDataCore/src /app/AnimalsDataCore/src
RUN mvn clean install -DskipTests

# Build datacollector
WORKDIR /app/datacollector

COPY datacollector/pom.xml /app/datacollector/pom.xml
RUN mvn dependency:go-offline -B

COPY datacollector/src /app/datacollector/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim AS production
RUN apt-get update && apt-get install -y wget

WORKDIR /app

# Copy the packaged application from the builder stage
COPY --from=builder /app/datacollector/target/*.jar datacollector.jar

## Run the Spring Boot application
CMD ["java", "-jar", "datacollector.jar"]