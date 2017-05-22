# MathRestTest

How to start the MathRestTest application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/math-1.0-SNAPSHOT.jar server config.yml`
`java -Djava.library.path=native -jar target/math-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`
http://localhost:8080/math/addv?v1=2&v2=3&v1=4&v2=5&v1=2&v2=3&v1=4&v2=5&v1=2&v2=3&v1=4&v2=5&v1=2&v2=3&v1=4&v2=5

Health Check
---
To see your applications health enter url `http://localhost:8081/healthcheck`

Create WAR - does it make sense
---
`mvn compile war:war`

