# JWT EXAMPLE
The project is mainly intended for educational purposes. Nevertheless you can download it and modify as you need.
The project demonstrates how to create and use JWT to protect Java applications.

## Implementation features
* JWT tokens are generated and validated using two different librarries (just for example):
  * [JSON Web Token Support For The JVM](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt)
  * [Spring Boot Starter OAuth2 Resource Server](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-resource-server)
* Switching between listed approaches is in application configuration
* Application uses two roles: `ADMIN` and `USER`, user with `ADMIN` role has access to `USER` role too

## Implementation details

## Generate keys
Before run the application you need to generate private and public RSA keys generate / validate JWT tokens.
To do this, go to `./src/main/resources/keys` directory and generate keys using `keygen` utility. Make sure length of private key cannot be less that 2048 bytes (OAUTH2 requirements).

## Run the application
Build the application as
```
mvn clean install
```
_Note: provate and public kays must be generated before application build._

Run the application as:
```
java -jar target/
```

## 

  
  
