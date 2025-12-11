# ------------ BUILD STAGE ------------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies before copying the full source
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN mvn -q dependency:go-offline

# Copy source
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# ------------ RUNTIME STAGE ------------
FROM eclipse-temurin:17-jdk AS runtime
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port used by your service
EXPOSE 8082

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
