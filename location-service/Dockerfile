# Use a base image with Java
FROM eclipse-temurin:17-jdk-alpine

# Set workdir
WORKDIR /app

# Copy build jar into the image
COPY target/location-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]