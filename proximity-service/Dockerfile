FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven build output into container
COPY target/proximity-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
