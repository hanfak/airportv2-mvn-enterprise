## To run

Prerequisites

Need to set the environment variable to $DATABASE_URL=jdbc:postgresql://127.0.0.1:5432/

1. mkdir -p $HOME/docker/volumes/postgres
2. docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres:11
3. cd flyway-sql
4. mvn compile flyway:migrate


To run build:

1. mvn clean verify

To run app:

1. go to com.hanfak.airport.wiring.Application and run main() methods

or

In progress,,,,
 - From Jar file
 - From Docker


Using Intellij

- right click test > Create ... and in pop up window, add under build a maven command for test-compile, delete main  and 

Address in use

- lsof -i tcp:1234
- kill -9 <PID>

Useful

- mvn findbugs:findbugs
- mvn org.apache.maven.plugins:maven-pmd-plugin:3.6:pmd
- view code coverage application/target/site/jacoco/index.html

Set the jvm options for maven as 
export MAVEN_OPTS=-noverify -XX:TieredStopAtLevel=1