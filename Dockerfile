# Use the official OpenJDK 8 image as base image
FROM openjdk:8-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container at position /app
COPY target/alphaX-${revision}.jar /app/alphaX-${revision}.jar

# Expose the port the app runs on
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "alphaX-${revision}.jar"]


