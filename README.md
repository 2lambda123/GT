
# Build Steps
* mvn install -DskipTests
* docker-compose up --build -d

* docker-compose -f docker-compose.backend.yml up --build -d (if you want to make UI separate)

* docker compose down

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

## The Names of Important Files are Denoted by Marvel Characters and Important Packages by Marvel Places
* Asgard -- The Main Booking engine
    * Loki does pre processing
    * Thor is does the actual processing
    * Heimdall does post processing
* Sokovia -- The Database Service
    * Wanda has all the logic to deal with the database actions (service)
    * Vision is gateway to Wanda (controller)
    * Pietro helps Wanda -- not as strong logic wise -- but is essential for the database logic to work properly
  
## Architecture

Client -> Load Balancer -> Loki -> Reverse Proxy -> Thor -> Heimdall -> Client