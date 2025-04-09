FROM openjdk:17

EXPOSE 8082

# Copier le fichier .jar dans l'image
ADD target/ordre-service-0.0.1-SNAPSHOT.jar ordre-service.jar

# Définir le point d’entrée pour exécuter l’application
ENTRYPOINT ["java", "-jar", "ordre-service.jar"]
