## Requirements

- maven version >= 3.6 or use the maven wrapper included in the project
- docker
- make sure to open project folder in terminal

## How to run

1. the command below will trigger docker to build the image including the application jar
   `mvn clean package` or `mvnw clean package`
2. to start the image and expose port 8080 on localhost run this command
   `docker run --name tarek-country-numbers-backend -d -p 8080:8080 tarek/country-numbers-backend`

---

## Single command

`mvn clean package && docker run --name tarek-country-numbers-backend -d -p 8080:8080 tarek/country-numbers-backend`

**Notes:**

- [Swagger ui](http://localhost:8080/swagger-ui.html)
- To stop the container run this `docker stop <container-id>`
