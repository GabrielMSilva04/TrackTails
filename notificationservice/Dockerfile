# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app

#COPY AnimalsDataCore /app/AnimalsDataCore
#WORKDIR /app/AnimalsDataCore
#RUN mvn dependency:go-offline -B
#RUN mvn clean install

COPY notificationservice /app/notificationservice
WORKDIR /app/notificationservice
RUN mvn dependency:go-offline -B
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

ENTRYPOINT ["/app/wait-for-it.sh", "mariadb:3306", "--", "java", "-jar", "app.jar"]

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
