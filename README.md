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


2. **Setting Up Environment Variables**

Before you start setting up environment variables, you should register on the official Twelve Data website to obtain an
API key (token). You can get it by following this [link](https://twelvedata.com/account/api-keys). After obtaining your
API key, you can proceed to set up environment variables.

**First Method: Using IntelliJ IDEA**

1. Open the main menu by pressing `Alt+\`, then select `Run -> Edit Configurations...`.
2. Click on `Modify Options` and choose `Environment Variables`.
3. Press `Shift + Enter` to start editing.
4. In the left column, enter `API_KEY`, and in the right column, input the key you received from the Twelve Data
   website.

**Second Method: Without Using IntelliJ IDEA**

1. Create a `.env` file in the root directory of your project.
2. Inside the `.env` file, add the value for `API_KEY`, which should look something like: `API_KEY=your_token_here`.

**Next Step**

1. Update your `docker-compose.yaml` file by replacing `API_KEY: your_api_key_here` with the actual key you obtained.

Congratulations, you're ready to move on to the next step!


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

