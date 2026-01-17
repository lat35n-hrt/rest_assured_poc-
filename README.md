# rest_assured_poc-


# rest_assured_poc

Minimal REST Assured PoC (JUnit5) for API testing.
This PoC is designed to be reproducible on macOS without installing Maven globally.

## Tech Stack
- Java 21 (Temurin)
- Maven Wrapper (`./mvnw`)
- REST Assured (API test DSL)
- JUnit 5 (test runner)
- MockWebServer (local stub server, no external API dependency)

## Why MockWebServer?
Public demo APIs may return 403 / rate-limit depending on network conditions.
This PoC uses a local stub server to keep tests deterministic and stable.

## Project Structure
```text
rest_assured_poc/
  pom.xml
  mvnw
  .mvn/wrapper/maven-wrapper.properties
  src/test/java/poc/TestConfig.java
  src/test/java/poc/ApiSmokeTest.java
```

## Run Tests

``` bash
./mvnw test
```

## Optional: Enable HTTP logging

``` bash
./mvnw -DhttpLog=true test
```

## Test Coverage (Smoke)

- GET /users?page=2 (status + JSON body assertions)

- POST /users (status + JSON body assertions)


## Note
This PoC avoids external public API dependencies by using MockWebServer, so the suite remains stable and reproducible.
