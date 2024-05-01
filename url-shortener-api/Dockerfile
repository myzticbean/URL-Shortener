# Use a slim Java base image
FROM openjdk:17-slim

# Set working directory
WORKDIR /app

# Copy the pom.xml file (assuming Maven project)
COPY pom.xml .

# Install dependencies using Maven
RUN mvn install -DskipTests

# Copy the application classes and resources
COPY target/*.jar app.jar

# Expose the port where your application listens
EXPOSE 8090

# Start the application by running the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
