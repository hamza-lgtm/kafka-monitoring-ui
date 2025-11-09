# Kafka Monitoring API

A Spring Boot application that provides REST APIs for monitoring and managing Apache Kafka clusters. This service offers comprehensive endpoints for topic management, consumer group operations, and message monitoring.

## Technologies

- Java 17
- Spring Boot 3.3.4
- Apache Kafka
- Spring Kafka
- Maven

## Prerequisites

- JDK 17 or higher
- Maven 3.6+
- Apache Kafka 2.x or higher
- Kafka running on localhost:9092 (default configuration)

## Getting Started

### Installation

1. Clone the repository:
```bash
git clone [repository-url]
cd kafka-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Topics

- **List All Topics**
  - `GET /kafka/topics`
  - Returns a list of all Kafka topics

- **Get Topic Metadata**
  - `GET /kafka/topics/{topic}`
  - Returns detailed metadata for a specific topic

- **Create Topic**
  - `POST /kafka/topics/create`
  - Request body:
    ```json
    {
      "topicName": "string",
      "numPartitions": int,
      "replicationFactor": int
    }
    ```

- **Delete Topic**
  - `DELETE /kafka/topics/{topic}/delete`
  - Deletes a specific topic

### Consumer Groups

- **List All Consumer Groups**
  - `GET /kafka/consumergroups`
  - Returns a list of all consumer groups

- **Get Consumer Groups for Topic**
  - `GET /kafka/topics/{topic}/consumergroups`
  - Returns consumer groups for a specific topic

- **Create Consumer Group**
  - `POST /kafka/consumergroups/{topic}/create`
  - Request body: groupId (String)

### Monitoring

- **Get Message Count**
  - `GET /kafka/consumergroups/{consumerGroupId}/topics/{topic}/messagecount`
  - Returns the message count for a specific consumer group and topic

- **Get Throughput Metrics**
  - `GET /kafka/topics/{topic}/throughput`
  - Returns throughput metrics for a specific topic

### Message Peek Operations

- **Peek Messages**
  - `GET /kafka/topics/{topic}/peek/{numMessages}`
  - Returns a specified number of messages without consuming them

- **Peek All Messages**
  - `GET /kafka/topics/{topic}/peekall`
  - Returns all messages from a topic without consuming them

## Configuration

The application can be configured through `application.properties`:

```properties
spring.application.name=kafka-api
spring.kafka.bootstrap-servers=localhost:9092
```

## Testing

Run the tests using Maven:

```bash
mvn test
```

The project includes:
- Unit tests for controllers and services
- Integration tests with embedded Kafka

## CORS Configuration

The API is configured to accept requests from `http://localhost:3000` by default. You can modify this in the `KafkaController` class.

## Development

The project uses Spring Boot DevTools for development, which provides:
- Automatic restart when files change
- Live reload
- Property defaults optimization

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

