## To run

Prerequisites

1. mkdir -p $HOME/docker/volumes/postgres
2. docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres:11
3. mvn compile flyway:migrate

To run build:

1. mvn clean verify

To run app:

1. go to com.hanfak.airport.wiring.Application and run main() methods

or

In progress,,,,
 - From Jar file
 - From Docker