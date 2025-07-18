# POC R2DBC

This repository is a parallel project to [Learning Webflux 3.0 (module `kotlin.06-data-access`)](https://github.com/fResult/Learn-Spring-Webflux-3.0/tree/main/kotlin/06-data-access) and is used for learning R2DBC with Spring WebFlux.

It was created separately because the original repository had connection issues with the R2DBC driver.\
This repository serves as a proof of concept (POC) to explore and test the R2DBC setup more reliably.

## Prerequisites

- Docker and Docker Compose

## Running the Project

### Build

```bash
./gradlew clean build
```

### Run App

#### `common` profile

```bash
SERVER_PORT=8089 \
MONGO_USER=mongo \
MONGO_PASSWORD=mongo \
MONGO_AUTH_DB=admin \
PG_USER=postgres \
PG_PASSWORD=postgres \
./gradlew bootRun
````

#### `springdata` profile

```bash
SERVER_PORT=8089 \
MONGO_USER=mongo \
MONGO_PASSWORD=mongo \
MONGO_AUTH_DB=admin \
PG_USER=postgres \
PG_PASSWORD=postgres \
./gradlew bootRun --args='--spring.profiles.active=springdata'
```

#### `mongo` profile

```bash
SERVER_PORT=8089 \
MONGO_USER=mongo \
MONGO_PASSWORD=mongo \
MONGO_AUTH_DB=admin \
PG_USER=postgres \
PG_PASSWORD=postgres \
./gradlew bootRun --args='--spring.profiles.active=mongo'
```

### Test

```bash
./gradlew test
```

### Testcontainers for Colima

Ref: https://java.testcontainers.org/supported_docker_environment/#colima

```bash
colima start --network-address
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="$HOME//.colima/default/docker.sock"
export TESTCONTAINERS_HOST_OVERRIDE=$(colima ls -j | jq -r '.address')
export DOCKER_HOST="unix://$HOME/.colima/default/docker.sock"
```

## Endpoints

### Functional Endpoints (`/fe/...`)

**Get All Customers:**

```bash
curl http://localhost:8089/fe/customers
```

**Get Customer by ID:**

```bash
curl http://localhost:8089/fe/customers/:id
```

### Rest Controller (`/rc/...`)

**Get All Customers:**

```bash
curl http://localhost:8089/rc/customers
```

**Get Customer by ID:**

```bash
curl http://localhost:8089/fe/customers/:id
```
