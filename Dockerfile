# ---------------------------------------------------------------- #
# Stage 1: Build the application (using a Maven/JDK image)
# ---------------------------------------------------------------- #
# Using a certified Eclipse Temurin JDK 21 image for the build stage
FROM maven:3.9-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file first to leverage Docker layer caching
COPY pom.xml .

# Copy the Maven wrapper files (if using a Maven wrapper)
COPY .mvn/ .mvn
COPY mvnw .

# Copy the rest of the source code
COPY src ./src

# Build the project, skipping tests for a faster deployment build
RUN mvn clean install -DskipTests


# ---------------------------------------------------------------- #
# Stage 2: Create the final, lightweight runtime image (JRE only)
# ---------------------------------------------------------------- #
# Using a smaller JRE-only image based on JDK 21 for the final artifact
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the executable JAR file from the 'build' stage into the final image
# We use *.jar to ensure the command works regardless of the version number
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]