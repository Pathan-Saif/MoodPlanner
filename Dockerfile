# Step 1: Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Step 2: Create volume for /tmp
VOLUME /tmp

# Step 3: Copy the Spring Boot jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Step 4: Run the jar
ENTRYPOINT ["java","-jar","/app.jar"]
