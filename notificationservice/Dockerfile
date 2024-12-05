# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app/notificationservice

# Uncomment this line if you are using AnimalDataCore as a dependency
#COPY AnimalsDataCore /app/AnimalsDataCore
#WORKDIR /app/AnimalsDataCore
#RUN mvn dependency:go-offline -B
#RUN mvn clean install

COPY notificationservice/pom.xml /app/notificationservice/pom.xml
RUN mvn dependency:go-offline -B
COPY notificationservice/src /app/notificationservice/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged application from the builder stage
COPY --from=builder /app/notificationservice/target/*.jar app.jar

# Expose the application's port (optional, change to your application's port)
EXPOSE 8080

COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

## Run the Spring Boot application
ENTRYPOINT ["/app/wait-for-it.sh", "mariadb:3306", "--", "java", "-jar", "app.jar"]