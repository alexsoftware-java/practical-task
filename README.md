# RateBoard practical task - Reservation API and messaging queues
## Overview 

![ReservationProcessingScheme.png](puml/ReservationProcessingScheme.png)

## Technologies

- **Spring Boot 3**: Leveraging the latest Spring Boot framework ensures high performance and maintainability.

- **Spring Data JPA**: Spring Data simplifies data access, making it easier to work with database (redis)

- **RabbitMQ**: Reliable and high-performance solution for fast messaging between services. (below you can find a compartment with Kafka and ActiveMQ)

- **Redis**: Fast key-value storage is used to store request meta-data.

- **Lombok**: Lombok reduces boilerplate code, improving code readability and maintainability.

- **Mockito, JUnit 5, WireMock and Testcontainers**: Rigorous testing methodologies guarantee the reliability of the service.

- **API Documentation**: Swagger has been integrated for clear and comprehensive API documentation, making it easy for users and developers to understand and interact with the service.

- **Nginx**: Simple nginx configuration allow us to scale up REST API service to provide necessary level of performance
## Getting Started

### This service requires only git and docker installed locally. All build steps will be performed inside docker containers.
#### Follow these steps to set up and run the Reservation Processor Service on your local machine using Docker:

1. Clone this repository to your local system using `git clone`.

2. Navigate to the project directory.

3. Run the application using docker-compose:
- build and run in background. Don't forget to change default API_KEY via env passing.
   ```shell
   docker-compose up -d
   ```
- check logs
   ```shell
   docker-compose logs -f
   ```
- stop the application
   ```shell
   docker-compose down
   ```
4. Access the API documentation at http://localhost:8080/swagger-ui/index.html to explore the available endpoints and requests examples and interact with the service.
