# Flight Tracker API

This is a robust backend API for a flight tracking application, built with Java and the Spring Boot framework. It provides a comprehensive set of endpoints to manage and retrieve data about cities, airports, aircraft, passengers, and flight schedules.

## Technologies Used
* **Java 17,21**
* **Spring Boot 3**
* **Spring Data JPA / Hibernate**
* **Maven**
* **PostgreSQL / MySQL**
* **Docker**
* **GitHub Actions** for CI/CD

## Features
* Full CRUD (Create, Read, Update, Delete) functionality for all core entities.
* RESTful API endpoints for fetching flight arrivals and departures.
* Handles complex relationships between entities (e.g., Many-to-Many, One-to-Many).
* Automated database schema management with `ddl-auto`.
* Packaged as a portable Docker container for easy deployment.

---

## API Endpoints
Here are some of the primary endpoints available:

| Method | Endpoint                                         | Description                                    |
| :----- | :----------------------------------------------- | :--------------------------------------------- |
| `GET`  | `/api/flights/departures?airportId={id}`         | Get all departing flights for a given airport. |
| `GET`  | `/api/flights/arrivals?airportId={id}`           | Get all arriving flights for a given airport.  |
| `GET`  | `/api/airports`                                  | Get a list of all airports.                    |
| `GET`  | `/api/cities`                                    | Get a list of all cities.                      |
| `POST` | `/api/cities`                                    | Create a new city.                             |

---

## Setup and Local Installation

### Prerequisites
* Java JDK 21
* Apache Maven
* Docker Desktop
* A running instance of PostgreSQL or MySQL.

### Running Locally
1.  **Clone the repository:**
    ```bash
    git clone (https://github.com/PrincessBz/FlightTrackerSprint.git)
    cd flightapi
    ```
2.  **Configure the database:**
    * Open the `src/main/resources/application.properties` file.
    * Update the `spring.datasource.url`, `username`, and `password` properties to point to your local database instance.
3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The API will be available at `http://localhost:8080`.

---

## Building and Deployment

### Build for Production
You can compile, test, and package the application into a runnable `.jar` file using Maven:
```bash
./mvnw clean package
```

BY PRINCESS

