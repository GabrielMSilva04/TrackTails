# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

COPY ./AnimalsDataCore/pom.xml /app/AnimalsDataCore/pom.xml 
WORKDIR /app/AnimalsDataCore
RUN mvn dependency:go-offline -B
COPY ./AnimalsDataCore/src /app/AnimalsDataCore/src
RUN mvn clean install

COPY actionservice/pom.xml /app/actionservice/pom.xml
WORKDIR /app/actionservice
RUN mvn dependency:go-offline -B
COPY actionservice/src /app/actionservice/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=builder /app/actionservice/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]