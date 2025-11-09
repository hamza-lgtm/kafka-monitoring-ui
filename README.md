## Kafka Monitoring UI

A lightweight monitoring and interaction project for Kafka consisting of:

- `kafka-api` — a Java (Spring Boot) backend that exposes HTTP endpoints to interact with Kafka topics and messages.
- `kafka-ui` — a React frontend that talks to the backend and provides a simple user interface for viewing/producing messages.

This repository contains both the backend API and the frontend UI to run locally or build for deployment.

## Repository structure

```
kafka-api/       # Spring Boot backend (Maven wrapper included)
kafka-ui/        # React frontend (package.json present)
```

For implementation details, check:

- `kafka-api/src/main/java/.../controller/KafkaController.java` — HTTP endpoints and API surface.
- `kafka-api/src/main/java/.../config/KafkaConfig.java` — Kafka client configuration.
- `kafka-ui/src/` — React components and services (e.g. `services/kafkaService.js`).

## Prerequisites

- Java (JDK) 17+ or the version required by the project (the project includes Maven wrapper).
- Maven (optional if using the included wrapper).
- Node.js (16+ recommended) and npm or yarn for the frontend.
- Access to a running Kafka cluster (bootstrap servers) for end-to-end usage.

## Backend — `kafka-api`

Location: `kafka-api`

Key files:

- `pom.xml` — Maven build file.
- `src/main/resources/application.properties` — runtime configuration.
- `src/main/java/.../controller/KafkaController.java` — REST endpoints.
- `src/main/java/.../config/KafkaConfig.java` — Kafka producers/consumers and connection settings.

Run (Windows PowerShell)

```powershell
cd kafka-api
.\mvnw.cmd spring-boot:run
```

Or build and run the jar:

```powershell
cd kafka-api
.\mvnw.cmd -DskipTests package
java -jar target/*.jar
```

Run tests:

```powershell
cd kafka-api
.\mvnw.cmd test
```

Configuration

Set Kafka-related properties in `kafka-api/src/main/resources/application.properties` or via environment variables. Typical properties include bootstrap servers and any security settings required by your Kafka cluster. See `KafkaConfig.java` for exact property keys used by the app.

API surface

Refer to `KafkaController.java` for the exact routes exposed by the backend. The controller implements the REST endpoints the frontend consumes (topic listing, producing messages, and retrieving messages). Example usage (adjust paths to the actual controller):

```bash
# List topics (example)
curl http://localhost:8080/api/topics

# Produce a message (example)
curl -X POST http://localhost:8080/api/topics/{topic}/messages -H "Content-Type: application/json" -d '{"key":"k","value":"hello"}'
```

## Frontend — `kafka-ui`

Location: `kafka-ui`

This is a React application that calls the backend API.

Install and run (PowerShell):

```powershell
cd kafka-ui
npm install
npm start
```

Build for production:

```powershell
cd kafka-ui
npm run build
```

Run tests:

```powershell
cd kafka-ui
npm test
```

Configuration

The frontend may read API-base URL and other settings from environment variables or a config file. By default, the React app is typically configured to call `http://localhost:8080` — if you run the backend on a different host/port, update the service base URL at `kafka-ui/src/services/kafkaService.js` or via environment variables during build.

## Docker (optional)

You can containerize both services.

- For the backend, build the app with Maven and create a Docker image using a JDK base image.
- For the frontend, build the static bundle (`npm run build`) and serve it with a static server (nginx or equivalent) in a container.

## Troubleshooting

- Backend fails to connect to Kafka: verify `bootstrap.servers` in `application.properties`, network connectivity, and any auth (SASL/SSL) settings.
- CORS or API call failures from the UI: ensure backend allows CORS or run the UI on the same host/port or use a proxy during development.
- Tests failing: run `mvnw.cmd test` for backend and `npm test` for frontend to see detailed failures.

## Contributing

1. Open an issue describing the bug or feature.
2. Create a branch: `git checkout -b feature/short-description`.
3. Implement changes and add tests where appropriate.
4. Run the test suites for both backend and frontend.
5. Open a pull request.

## References & Notes

- The backend uses Spring Boot (Maven wrapper present).
- The frontend is a Create React App project (check `package.json` for scripts and dependencies).
- For implementation details and exact API contract, read `kafka-api/src/main/java/.../controller/KafkaController.java` and `kafka-ui/src/services/kafkaService.js`.

