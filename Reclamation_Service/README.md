# 🛠️ Reclamation Service

This microservice is responsible for handling user **reclamations (complaints/feedback)** in a distributed microservice-based application.

## 📦 Technologies Used

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Cloud (Eureka Client, Config)
- H2 Database
- Docker

## 🚀 How to Run

### Prerequisites

- JDK 17+
- Maven
- Docker (optional)
- Eureka Server running on `http://localhost:8761/`
- Spring Cloud Config Server running on `http://localhost:8888/`

### Run with Maven

```bash
mvn clean install
mvn spring-boot:run
Run with Docker
First, build the JAR:

bash
Copy
Edit
mvn clean package
Then build and run the Docker container:

bash
Copy
Edit
docker build -t reclamation_service .
docker run -p 8084:8084 --name reclamation_service reclamation_service
Make sure port 8084 is free or adjust it as needed.

🌐 Endpoints
Method	Endpoint	Description
GET	/reclamations/hello	Test endpoint to say hello
(More)	(To be expanded)	Add other endpoints as needed
⚙️ Configuration
application.properties includes:

properties
Copy
Edit
spring.application.name=ReclamationService
server.port=8084

# H2 Database
spring.h2.console.enabled=true
spring.h2.console.path=/h2
spring.datasource.url=jdbc:h2:file:./Database/Data/reclamation
spring.datasource.username=malek
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create

# Eureka
eureka.client.register-with-eureka=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Config Server
spring.cloud.config.enabled=true
spring.config.import=optional:configserver:http://localhost:8888
📁 Project Structure
swift
Copy
Edit
Reclamation_Service/
├── src/
│   ├── main/java/
│   │   └── tn/esprit/microservice/reclamation_service/
│   │       ├── ReclamationRestAPI.java
│   │       └── ... (other classes)
│   └── resources/
│       └── application.properties
├── Dockerfile
├── pom.xml
└── README.md  ← You are here
🧪 Testing
You can test the basic endpoint:

bash
Copy
Edit
curl http://localhost:8084/reclamations/hello
🧑‍💻 Author
Dridi Chaher – Intern at Banque de Tunisie

GitHub: chahe-dridi

This microservice is part of the distributed project: distributed-web-application