# HearIt

## Backend

Backend development was made with Spring Boot in a monolithic architecture, because of time constraints, with connection to the Spotify API through the _Authorization Code Flow_.

### Set up MySQL databse

This project connects to a MySQL database named `hearIt` and it is authenticated with the following credentials:
- Username: `hearItDBUser`
- Password: `S3CRET_Password`

The configuration of this setup is up to the user, needing to change spring.datasource.[url, username, password] properties in the `/application.yml` file.

To create the specified database and user follow the next instructions:
```bash
# Log into MySQL CLI
$ mysql -u {USERNAMA} -p {PASSWORD}

# Create database
mysql> CREATE DATABASE IF NOT EXISTS hearIt;

# Create user for the specific HearIt database
mysql> CREATE USER 'hearItDBUser'@'localhost' IDENTIFIED BY 'S3CRET_Password';

# Grant all permissions (discouraged!)
mysql> GRANT ALL ON hearIt.* TO 'hearItDBUser'@'localhost';

# Exit MySQL CLI
mysql> exit
```


### Install and run the server

```bash
# Clone repository
$ git clone https://github.com/LMartinezEXEX/HearIt.git && cd HearIt

# Go to backend directory
$ cd hearIt_backend

# Install dependencies and run it with maven
$ mvn spring-boot:run
```

> The server will run on `localhost`, port `8080`.

### Test

Some basic test for the controllers were made, they can be run with the following bash command:

```bash
# Inside hearIt_backend
$ mvn test
```

## Frontend

Frontend developed with Angular CLI which connects to the Backend through `localhost` port 8080.

### Install and run the server

```bash
# If you havent already cloned the repository
$ git clone https://github.com/LMartinezEXEX/HearIt.git && cd HearIt

# Go to frotnend directory
$ cd hearIt_frontend

# Install npm dependencies
$ npm install

# Start the server
$ ng serve
```

> This will run on `localhost`, port `4200`. This **should not be changed** due to Spotify API redirection to this location.