# Transaction-Service

## **Description**

This microservice is designed for handling transactions, monitoring expenses in various currencies, and controlling
expense limits.

---

## **Key Features**

- **Transaction Processing**: Storing information about expense operations in different currencies (USD, EUR, RUB,
  etc.).
- **Expense Limit Control**: Storing and verifying monthly expense limits in USD.
- **Currency Exchange Rates**: Integration with an external API (TwelveData API) to fetch currency exchange rates (
  EUR/USD, RUB/USD) and store them in the database.
- **Flexible Limit Configuration**: Ability to set new expense limits considering the current date.

---

## **Technologies**

- **Spring Boot**: Primary framework for developing the service.
- **PostgreSQL**: Main database for storing entities (transactions, limits).
- **MongoDB**: NoSQL database for storing currency exchange rates.
- **Liquibase**: Database migration tool.
- **TwelveData API**: External data source for fetching currency exchange rates.
- **Docker**: Containerization of the service to simplify deployment.

---

## **Installation Guide**

### **Prerequisites**

Ensure that Docker is installed on your computer.

### **Installation and Startup Steps**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/dzenthai/Transaction-Service.git
   cd transaction-service

2. **Build Docker Image**
   Create a Docker image for your service:
   ```bash
   docker build -t transaction-service .

3. **Run Docker Compose**
   Deploy all necessary services by running docker-compose.yml:
   ```bash
   docker-compose up

4. **Access the Application**
   After starting the Docker containers, the application can be accessed at http://localhost:8080.

5. **Database Initialization**
   The application will automatically create the necessary tables in the database upon startup.

6. **Application Configuration**
   After installation, configure the application properties (e.g., application.properties or application.yml)
   to specify environment parameters such as database URL, tokens, etc.

---

## **Additional Information**

- **Swagger**: Use Swagger UI for viewing and testing the API, available at http://localhost:8080/swagger-ui.html.
- **Postman Collection**: A postman_collection.json file is also available in the project for testing the API in
  Postman.
- **External API Integration**: Ensure the service integrates correctly with external APIs, such as TwelveData, to fetch
  up-to-date currency exchange rates.

