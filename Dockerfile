# Use a lightweight JDK
FROM amazoncorretto:21-alpine


# Set working directory inside container
WORKDIR /app

# Copy Gradle build output (jar file) into container
COPY build/libs/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
