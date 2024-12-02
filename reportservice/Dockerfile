FROM maven:3.8.5-openjdk-17 AS builder

# Build AnimalsDataCore
WORKDIR /app/AnimalsDataCore
COPY AnimalsDataCore/pom.xml /app/AnimalsDataCore/pom.xml
RUN mvn dependency:go-offline -B
COPY AnimalsDataCore/src /app/AnimalsDataCore/src
RUN mvn clean install


WORKDIR /app/reportservice
COPY reportservice/pom.xml /app/reportservice/pom.xml
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/reportservice/target/*.jar app.jar

EXPOSE 8080

COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

ENTRYPOINT ["/app/wait-for-it.sh", "mariadb:3306", "--", "java", "-jar", "app.jar"]