# Step 1: Use OpenJDK 17 as base image
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and src folder
COPY pom.xml .
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# Use minimal OpenJDK image for running
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
