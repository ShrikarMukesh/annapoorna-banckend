# Use the official OpenJDK image as the base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file and the logback.xml file to the container
COPY target/restaurant-service.jar /app/restaurant-service.jar
COPY logback.xml /app/logback.xml

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Specify the command to run your Spring Boot application
CMD ["java", "-Dspring.profiles.active=prod", "-Dlogging.config=/app/logback.xml", "-jar", "/app/restaurant-service.jar"]
