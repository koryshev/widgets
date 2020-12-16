## Java REST Web service with Spring Boot

### Notes
* The application supports two Spring profiles that use different implementation of data storage: `in-memory` (default) and `jpa`.
* The `jpa` profile uses the in-memory database H2 as data storage. H2 web console is available at: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, username: `sa`, empty password).
    * The database is populated with some test data on application startup.
* REST API is documented using Swagger, a web UI is available at `http://localhost:8080/swagger-ui.html` 

### Build requirements
* JDK 11
* Maven 3

### How to run
1. Build the project and run the tests.

    ```bash
    $ mvn clean package
    ```

2. Run the application.

* With the `in-memory` profile:

    ```bash
    $ java -jar target/widgets.jar
    ```

* With the `jpa` profile:

    ```bash
    $ java -jar -Dspring.profiles.active=jpa target/widgets.jar
    ```
