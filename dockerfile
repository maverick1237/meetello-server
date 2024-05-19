# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application's jar file into the container
COPY ./target/meetello-server-0.0.1-SNAPSHOT.jar /app/myapp.jar

# Make port 8080 available to the world outside this container
EXPOSE 8000

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/myapp.jar","--spring.profiles.active=prod"]
