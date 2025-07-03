Flight Tracker REST API
This repository contains the backend RESTful API for the Flight Tracker application. It is built using Spring Boot and manages data related to cities, airports, passengers, and aircrafts, providing a set of endpoints for data retrieval and manipulation.

- Table of Contents
●	Project Purpose
●	Requirements
●	Local Setup and Installation
●	API Endpoints
●	Data Model
●	Testing
●	GitHub Actions (CI)
●	Project Structure

- Project Purpose
The Flight Tracker REST API serves as the data management layer for the Flight Tracker application. It exposes a set of RESTful endpoints that allow clients (like the Flight Tracker CLI Client) to perform CRUD (Create, Read, Update, Delete) operations on the core entities:
●	City: Manages city information.
●	Airport: Manages airport details, linked to cities.
●	Passenger: Manages passenger information, linked to cities and aircrafts.
●	Aircraft: Manages aircraft details, linked to airlines, passengers, and airports.
It handles data persistence, typically using a relational database (e.g., PostgreSQL).

- Requirements
To build and run this API application, you will need:
●	Java Development Kit (JDK) 17 or higher (matching your pom.xml configuration).
●	Apache Maven 3.6.0 or higher.
●	PostgreSQL Database: A running PostgreSQL instance is required for data persistence.
●	Spring Boot 3.x.x compatible environment.

- Local Setup and Installation
  
Follow these steps to get the API running on your local machine:
1.	Clone the Repository: git clone https://github.com/PrincessBz/FlightTrackerSprint.git
2.	Database Setup (PostgreSQL):
○	Ensure you have a PostgreSQL server running.
○	Create a new database for this application (e.g., flighttracker_db).
○	Update the src/main/resources/application.properties (or application.yml) file with your database connection details (username, password, database name). Example application.properties: spring.datasource.url=jdbc:postgresql://localhost:5432/flighttracker_db spring.datasource.username=your_db_username spring.datasource.password=your_db_password spring.jpa.hibernate.ddl-auto=update # or create, create-drop for development spring.jpa.show-sql=true 
3.	Build the Project: Navigate to the root directory of the cloned FlightTrackerAPI project and build it using Maven. mvn clean install 
4.	Run the API Application: You can run the Spring Boot application directly from Maven: mvn spring-boot:run  The API should start and be accessible, by default, at http://localhost:8080.

- API Endpoints

The API exposes RESTful endpoints for the core entities. The base path for all API endpoints is typically /api.
●	Cities:
○	GET /api/cities: Get all cities.
○	GET /api/cities/{id}: Get a city by ID.
○	GET /api/cities/{cityId}/airports: Get all airports in a specific city.
○	POST /api/cities: Create a new city.
○	PUT /api/cities/{id}: Update an existing city.
○	DELETE /api/cities/{id}: Delete a city.
●	Airports:
○	GET /api/airports: Get all airports.
○	GET /api/airports/{id}: Get an airport by ID.
○	POST /api/airports: Create a new airport.
○	PUT /api/airports/{id}: Update an existing airport.
○	DELETE /api/airports/{id}: Delete an airport.
●	Passengers:
○	GET /api/passengers: Get all passengers.
○	GET /api/passengers/{id}: Get a passenger by ID.
○	GET /api/passengers/{passengerId}/aircrafts: Get aircrafts a specific passenger has flown on.
○	GET /api/passengers/{passengerId}/airportsUsed: Get airports a specific passenger has used.
○	POST /api/passengers: Create a new passenger.
○	PUT /api/passengers/{id}: Update an existing passenger.
○	DELETE /api/passengers/{id}: Delete a passenger.
●	Aircrafts:
○	GET /api/aircrafts: Get all aircrafts.
○	GET /api/aircrafts/{id}: Get an aircraft by ID.
○	GET /api/aircrafts/{aircraftId}/airports: Get airports an aircraft takes off from and lands at.
○	POST /api/aircrafts: Create a new aircraft.
○	PUT /api/aircrafts/{id}: Update an existing aircraft.
○	DELETE /api/aircrafts/{id}: Delete an aircraft.
(Note: The exact endpoints may vary slightly based on your controller implementations.)

- Data Model

The API manages four core entities with the following relationships:
●	City: (id, name, state, population)
●	Airport: (id, name, code)
○	A City has many Airports.
○	An Airport belongs to one City.
●	Passenger: (id, firstName, lastName, phoneNumber)
○	A Passenger lives in one City (implied, often via a city_id foreign key).
○	A Passenger can fly on many Aircraft (many-to-many relationship).
●	Aircraft: (id, type, airlineName, numberOfPassengers)
○	An Aircraft can carry many Passengers (many-to-many relationship).
○	An Aircraft can use many Airports (for takeoff/landing, many-to-many relationship).
Testing
The project includes unit and integration tests for the API's services and controllers.
To run the tests:
mvn test 
Or, as part of the full build:
mvn clean install 

- GitHub Actions (CI)
This repository can be configured with a GitHub Actions workflow (e.g., .github/workflows/maven.yml) to enable Continuous Integration (CI).
●	Any push to the main branch or pull_request targeting main will automatically trigger a build and run all unit/integration tests.
●	You can view the status of these automated checks under the "Actions" tab of this GitHub repository.
Project Structure
FlightTrackerAPI/
├── pom.xml                                  # Maven Project Object Model
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── flighttracker/
│   │   │           └── api/                 # Base package for the API
│   │   │               ├── controller/      # REST API endpoints
│   │   │               ├── model/           # JPA Entities (City, Airport, Passenger, Aircraft)
│   │   │               ├── repository/      # Spring Data JPA repositories
│   │   │               ├── service/         # Business logic services
│   │   │               └── FlightTrackerApiApplication.java # Main Spring Boot application class
│   │   └── resources/
│   │       ├── application.properties       # Spring Boot configuration (database, port, etc.)
│   │       └── data.sql                     # Optional: SQL script for initial data
│   └── test/
│       └── java/
│           └── com/
│               └── flighttracker/
│                   └── api/
│                       ├── controller/      # Tests for controllers
│                       └── service/         # Tests for services
└── .github/
    └── workflows/
        └── maven.yml                        # Optional: GitHub Actions CI workflow



