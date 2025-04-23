# Use the official Maven image to build the project
FROM maven:3.8.5-openjdk-17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies (this will be cached for efficiency)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code and compile the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK 17 slim image for the runtime environment
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/EurekaApplication-0.0.1-SNAPSHOT.jar /app/eureka-server.jar

# Expose the port that Eureka Server will run on
EXPOSE 8761

# Run the JAR file
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
