FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app/gateway

COPY gateway/pom.xml /app/gateway/pom.xml
RUN mvn dependency:go-offline -B

COPY gateway/src /app/gateway/src
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/gateway/target/*.jar app.jar
EXPOSE 8080
COPY .env /app/.env

COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

ENTRYPOINT ["/app/wait-for-it.sh", "-t", "60", "userservice:8080", "--", "java", "-jar", "app.jar"]