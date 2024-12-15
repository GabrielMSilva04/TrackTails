# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app/notificationservice
COPY notificationservice/pom.xml /app/notificationservice/pom.xml
RUN mvn dependency:go-offline -B

COPY notificationservice/src /app/notificationservice/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim AS production
RUN apt-get update && apt-get install -y wget

# Set the working directory in the container
WORKDIR /app

# Copy the packaged application from the builder stage
COPY --from=builder /app/notificationservice/target/*.jar notification-service.jar

## Run the Spring Boot application
CMD ["java", "-jar", "notification-service.jar"]