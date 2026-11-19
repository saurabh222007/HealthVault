# ========================================================
# HealthVault Cloud - Multi-Stage Dockerfile for Cloud Run
# Stage 1: Build the Spring Boot application using Maven
# ========================================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the executable jar without running tests
RUN mvn clean package -DskipTests

# ========================================================
# Stage 2: Minimal Java Runtime Environment for Production
# ========================================================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/healthvault-1.0.0.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "app.jar"]
