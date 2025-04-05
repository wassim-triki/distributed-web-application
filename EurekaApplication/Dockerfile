FROM openjdk:17
EXPOSE 8761

# Copy the JAR file into the container
ADD target/EurekaApplication-0.0.1-SNAPSHOT.jar /eureka.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/eureka.jar"]
