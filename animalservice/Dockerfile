# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

# Build AnimalsDataCore
WORKDIR /app/AnimalsDataCore
COPY AnimalsDataCore/pom.xml /app/AnimalsDataCore/pom.xml
COPY AnimalsDataCore/src /app/AnimalsDataCore/src
RUN mvn clean install -DskipTests

# Build animalservice
WORKDIR /app/animalservice
COPY animalservice/pom.xml /app/animalservice/pom.xml
COPY animalservice/src /app/animalservice/src
RUN mvn clean package -DskipTests

# Stage 2: Production Runtime
FROM openjdk:17-jdk-slim AS production

WORKDIR /app
COPY --from=builder /app/animalservice/target/*.jar /app/animalservice.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/animalservice.jar"]

# Stage 3: Development Environment with Hot Reload
FROM maven:3.8.5-openjdk-17 AS development

WORKDIR /app
COPY animalservice /app
COPY AnimalsDataCore /app/AnimalsDataCore
EXPOSE 8080

CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev"]
