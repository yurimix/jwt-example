# JWT EXAMPLE
The project is mainly intended for educational purposes. Nevertheless you can download it and modify as you need.
The project demonstrates how to create and use JWT to protect Spring-based Java applications.

## Implementation features
* JWT tokens are generated and validated using two different signers (just for example):
  * [JSON Web Token Support For The JVM](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt)
  * [Spring Boot Starter OAuth2 Resource Server](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-resource-server)
* Switching between listed signers is in application configuration ()
* The application authenticates any user that matches the `name == password` rule, for example: `user:user` or `developer:developer`
* Predefined user names (see `dev.example.jwtdemo.service.DefaultUserDetailsService`):
  * admin - admin user
  * expired - expired user (`User account has expired` message)
  * locked - locked user (`User account is locked` message)
*  There are two roles: `ADMIN` and `USER`, an ordinary user has USER role, admin user has both roles.

_**Note:** using `jjwt` here is just for a joke, I would recommend OAUTH2 if you want to do production code. But the choice is yours._

## Implementation details

## Generate keys
Before run the application you need to generate private and public RSA keys to manage JWT tokens.
You can do this using the `./src/main/resources/keys/keygen` utility or any other key generator what you want.
But, in any case, make sure length of private key cannot be less that 2048 bytes.

## Run the application
_**Note:** private and public kays must be generated before application run._

Run the application:
```
mvn spring-boot:run

```
Run application tests:
```
mvn test
```

### Authentication token
Get authentication token:
```
curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/authenticate --data '{"username":"test_user","password":"test_user"}'
```
Example of response body:
```
{"token":"<base64-encoded-token-data>"}
```
### Access to app
#### Access to user's resource
```
curl -X GET -H 'Authorization: Bearer <base64-encoded-token-data>' -i http://localhost:8080/hello/user
```  
Response
```
Hello USER!
```
#### Access to admin's resource
```
curl -X GET -H 'Authorization: Bearer <base64-encoded-token-data>' -i http://localhost:8080/hello/admin
```  
If you are using a token generated as described above, you will see a 403 error.
To have access to admin's resource you must to generate a new token as '{"username":"admin","password":"admin"}'.
Using the new token the response will be
```
Hello ADMIN!
```
This token is suitable to have access to `/hello/user` too.

**That's all.**

**PS:** Visit to https://jwt.io to view generated tokens data.
