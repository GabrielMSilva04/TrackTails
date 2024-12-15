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

WORKDIR /app

COPY --from=builder /app/userservice/target/*.jar app.jar
EXPOSE 8080

COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

## Run the Spring Boot application
ENTRYPOINT ["/app/wait-for-it.sh", "mariadb:3306", "--", "java", "-jar", "app.jar"]