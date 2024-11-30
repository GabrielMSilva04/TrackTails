# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app/actionservice

# Uncomment this line if you are using AnimalDataCore as a dependency
#COPY AnimalsDataCore /app/AnimalsDataCore
#WORKDIR /app/AnimalsDataCore
#RUN mvn dependency:go-offline -B
#RUN mvn clean install

COPY actionservice/pom.xml /app/actionservice/pom.xml
RUN mvn dependency:go-offline -B
COPY actionservice/src /app/actionservice/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged application from the builder stage
COPY --from=builder /app/actionservice/target/*.jar app.jar

# Expose the application's port (optional, change to your application's port)
EXPOSE 8080

## Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]