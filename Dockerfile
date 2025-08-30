# Use a minimal JDK image for running the application.
# OpenJDK 17 is a good choice for modern Spring Boot applications.
FROM openjdk:17-jdk-slim

# Set the working directory inside the container.
WORKDIR /app

# Copy the built JAR file from your Maven target directory.
# This is the corrected line using your project's artifactId and version.
COPY target/IntegrationAPI-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application runs on.
# The default is 8080.
EXPOSE 8080

# Define the command to run the application when the container starts.
# This executes the Java application from the copied JAR.
CMD ["java", "-jar", "app.jar"]