# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

COPY AnimalsDataCore /app/AnimalsDataCore
WORKDIR /app/AnimalsDataCore
RUN mvn dependency:go-offline -B
RUN mvn clean install

WORKDIR /app/datacollector

COPY datacollector/pom.xml /app/datacollector/pom.xml
RUN mvn dependency:go-offline -B
COPY datacollector/src /app/datacollector/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/datacollector/target/*.jar app.jar

EXPOSE 8080

COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

ENTRYPOINT ["/app/wait-for-it.sh", "mariadb:3306", "--", "java", "-jar", "app.jar"]