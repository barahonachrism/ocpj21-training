# Java SE 21 Exam Simulator - Backend

This is the backend component of the Oracle Java SE 21 Developer Exam Simulator. It is built using Spring Boot and provides a RESTful API for managing exams and questions.

## Technologies Used

- **Java 21**: The latest LTS version of Java.
- **Spring Boot 3.2.1**: Framework for building production-ready applications.
- **Spring Data JPA**: For data persistence.
- **H2 Database**: An in-memory database for development and testing.
- **Maven**: Project management and build tool.

## Project Structure

The project follows the standard Maven directory structure:

- `src/main/java`: Contains the application source code.
  - `com.ocpj21.simulator.controller`: REST controllers.
  - `com.ocpj21.simulator.service`: Business logic and services.
  - `com.ocpj21.simulator.model`: Entity classes.
  - `com.ocpj21.simulator.repository`: Data access repositories.
- `src/main/resources`: Configuration files and static resources.
- `src/test/java`: Unit and integration tests.

## Getting Started

### Prerequisites

- Java 21 JDK
- Maven

### Running the Application

To run the application locally, use the following command:

```bash
mvn spring-boot:run
```

The backend will be available at `http://localhost:8080`.

## API Endpoints

- `POST /api/exam/start`: Starts a new exam.
- `GET /api/exam/{id}`: Retrieves an exam by ID.
- `POST /api/exam/{id}/submit`: Submits answers for an exam.

## Question Loading

The application automatically loads questions from Markdown files located in the path specified by the `book.path` property in `application.properties`.
