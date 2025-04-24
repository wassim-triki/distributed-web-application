# Use the official Maven image to build the project
FROM maven:3.8.5-openjdk-17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download the dependencies (this will be cached for better performance)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code and compile the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK 17 for running the app (the runtime image)
FROM openjdk:17-slim

# Set the working directory for the application inside the container
WORKDIR /app

# Copy the JAR from the build stage into the current directory of the runtime image
COPY --from=build /app/target/mini_projet_panier-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that the app will run on
EXPOSE 8082

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
