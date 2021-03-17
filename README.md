
# Build Steps
* mvn install -DskipTests
* docker-compose up --build -d

* docker-compose -f docker-compose.backend.yml up --build -d (if you want to make UI separate)

# Post Request
* Specify ID: {"id":"3","symbol":"c","quantity":100}
* Allow auto generation of ID: {"symbol":"c","quantity":100}

## Prerequisites
* [Docker](), [Git](https://git-scm.com/), [Maven](), [JDK 7 or 8](), [Eclipse or VSCode](), [Postman]()

## Get And Run This Project
Run these commands one by one into cmd/terminal/shell.
* Clone this repository: ``
* Get into cloned folder: `cd `
* Build project: `mvn install` or `mvn clean install` or  `mvn install -DskipTests`
* Run docker compose: `docker-compose up --build -d`
* Visit app at [http://localhost:8084](http://localhost:8084) from browser, explore endpoints using `Postman`.
* To stop all services: `docker-compose down`
