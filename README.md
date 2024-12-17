# Eiffel Bike Corp

Eiffel Bike Corp is a bike rental and sales service application. This project is built using Java and Maven, and it provides a RESTful API for managing bike rentals and sales. Beyond the project itself, it includes a report and this `README.md` file, which serves as a user manual.

## Authors

Miguel Valadares - nº 283781, Miguel Cordeiro - nº 283767

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/MLValadares/EiffelBikeCorp.git
    cd EiffelBikeCorp
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

3. Install Apache Tomcat:
    - Download Apache Tomcat from the [official website](https://tomcat.apache.org/download-90.cgi).
    - Extract the downloaded archive to a directory of your choice.

4. Deploy the application to Tomcat:
    - Copy the generated WAR file from the `target` directory (e.g., `EiffelBikeCorp.war`) to the `webapps` directory of your Tomcat installation.

5. Start the Tomcat server:
    - Navigate to the `bin` directory of your Tomcat installation.
    - Run the startup script:
        - On Windows: `startup.bat`
        - On Unix-based systems: `./startup.sh`

## Usage

The application will be available at `http://localhost:8080/EiffelBikeCorp/api`
To check if the application is running, you can open a browser with: `http://localhost:8080/EiffelBikeCorp/api/hello-world`

## API Endpoints

### Bike Rental

- **Get all bikes**
    ```http
    GET /api/bikeRental
    ```

- **Rent a bike**
    ```http
    POST /api/bikeRental/rent/{id}
    Headers: Authorization: <token>
    Query Params: waitingList (optional, default: false)
    ```

- **Return a bike**
    ```http
    POST /api/bikeRental/return/{id}
    Headers: Authorization: <token>
    Query Params: conditionRating, conditionNotes (optional)
    ```

- **Add a new bike**
    ```http
    PUT /api/bikeRental/add
    Headers: Authorization: <token>
    Body: { "model": "Bike Model" }
    ```

- **Remove a bike**
    ```http
    DELETE /api/bikeRental/remove/{bikeId}
    Headers: Authorization: <token>
    ```

### Gustave Bike Sales

- **Get available bikes for sale**
    ```http
    GET /api/bikeRental/gustaveBike/availableBikes
    ```

- **Add a bike to basket**
    ```http
    POST /api/bikeRental/gustaveBike/basket/{bikeId}
    Headers: Authorization: <token>
    ```

- **Get basket**
    ```http
    GET /api/bikeRental/gustaveBike/basket
    Headers: Authorization: <token>
    Query Params: currency (optional)
    ```

- **Buy basket**
    ```http
    POST /api/bikeRental/gustaveBike/basket/buy
    Headers: Authorization: <token>
    Query Params: currency (optional)
    ```


## Currency API used
https://github.com/fawazahmed0/exchange-api
