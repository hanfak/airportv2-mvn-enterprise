### Task


We have a request from a client to write the software to control the flow of planes at an airport. The planes can land and take off provided that the weather is sunny. Occasionally it may be stormy, in which case no planes can land or take off. Here are the user stories that we worked out in collaboration with the client:


```
As an air traffic controller
So I can get passengers to a destination
I want to instruct a plane to land at an airport and confirm that it has landed

As an air traffic controller
So I can get passengers on the way to their destination
I want to instruct a plane to take off from an airport and confirm that it is no longer in the airport

As an air traffic controller
To ensure safety
I want to prevent takeoff when weather is stormy

As an air traffic controller
To ensure safety
I want to prevent landing when weather is stormy

As an air traffic controller
To ensure safety
I want to prevent landing when the airport is full

As the system designer
So that the software can be used for many different airports
I would like a default airport capacity that can be overridden as appropriate
```


Feature TODO:

- Should we be talking about hangers or just airport?
- Include gate (when landed, proceed to empty gate (using service))
- plane status service, to change the status and check the status of the plane, turn to db
-


Technical TODO:

- ~~clean architecture maintained via domain-enforcer libray~~
- Have an application start up(main, property file for prod)
    - -simple wiring and application class-
    - configuration, wiring
    - simple main, with command line interface
- ~~Add Exceptions, then try catch them and add logs with stack trace~~
    - ~~slf4j~~
- ~~plant uml~~
    - use intellij plugin to generate this file
    - TODO: generate using code ie workflow
- properties
    - property loader
    - test and prod
    - default & enhanced prop
- Add db, object pooling, property file
    - mysql/postgres
    - c3po or hikari pooling
    - use in memory db ie H2 and properties and code (extra methods on db) just for tests
    - use stub (list) for database in test
    - create users, owenrs with rights
    - editioning and views
- flyway db to setup database
    - maven, module
- Add Jetty server, split up usecase into several classes
    - property file
    - respond with json, custom marshaller and unmarshaller
    - acceptance tests are run via server then docker container
    - post req for takeoff and landing
    - get req for plane status
    - logging using logbook
    - log incoming and outgoing requests
- Access and audit logging
- status page, check db & weather service is up, scheduled job every minute
- ~~use testlogger~~
- Yatspec
    - dictionary (see shelf)
    - Use givenWhenThen from TestState (Move to another test, with different way of asserting on output using capturedInputsAndOutputs)
        - for learning test with webserver
    - interesting givens
    - sequence diagram
    - interestingGivens.getType
    - plant uml for acceptance test
- Use third party weather service, use wire mock for stub, http client to talk wiht it
    - https://fcc-weather-api.glitch.me/api/current?lat=51.4700&lon=0.4543
    - https://www.metaweather.com/api/
    - logging
    - timeout
- wiring, split for third party int, database
- separate acceptance test into module
- ~~find bugs Static analysis via maven~~
    - mvn findbugs:findbugs
        - issues with findbugs???
    - mvn org.apache.maven.plugins:maven-pmd-plugin:3.6:pmd
- scheduler to check weather, and store in cache (Db - redis)
    - quartz, cron
- Use Akka to make calls to db async
- cache for weather service (redis)
- dockerise, use maven to dockerise and run acceptance tests through image
- metrics end point, prometheues
- jenkins ci build
- extract service to separate app (database)
    - use docker
    - kubernetes, multiple replicas
        - pre install hook for migrating and populating db
    - encrypt password, decrypt secrets
    - traceyId
    - wiremock
    - contract tests
- batch/aysnc jobs to other service
    - refuel, unload, clean up, inspect
    - messaging service
    - concurrent
    - soap, ftp, amq,
- cqrs and event sourcing, aggregates for flow
- gui
- implement using spring, other libraries
