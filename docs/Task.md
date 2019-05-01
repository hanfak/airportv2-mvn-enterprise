### Task


We have a request from a client to writeASingleRecord the software to control the flow of planes at an airport. The planes can land and take off provided that the weather is sunny. Occasionally it may be stormy, in which case no planes can land or take off. Here are the user stories that we worked out in collaboration with the client:


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

- ~~clean architecture maintained~~
    - domain-enforcer libray
    - domain objects - immutable
    - dependency inversion for inner layers to communicate with outer layers
    - use case - shows flow of single application action

- Multiple maven modules
    - app
    - flyway

- Have an application start up
    - -simple wiring and application class-
    - configuration, wiring
    - ~~singletons for instantiating specific objects~~
    - wiring, split for third party int, database

- ~~Add Exceptions, then try catch them and add logs with stack trace~~

- ~~plant uml~~
    - use intellij plugin to generate this file
    - TODO: generate using code ie workflow

- properties
    - ~~property loader~~
    - test and prod
    - ~~default & enhanced prop~~
    - test properties

- Add db, object pooling, property file
    - ~~postgres~~
    - ~~c3po or hikari for object pooling~~
    - use in memory db ie H2 and properties and code (extra methods on db) just for tests
    - use stub (list) for database in test
    - editioning and views

- flyway db to setup database
    - ~~maven~~
    - afterMigrate create user
    - create users, owenrs with rights
    - clean db, drop users

- Add Jetty server, split up usecase into several classes
    - property file
    - respond with xml, custom marshaller and unmarshaller
    - acceptance tests are run via server then docker container
    - post req for takeoff and landing
    - get req for plane status
    - ready page
    - ~~logging using logbook~~
        - ~~log incoming and outgoing requests~~
    - 404 page with links for all endpoints
        - hide links

- status page,
    - check db & weather service is up,
    - scheduled job every minute
    - use in memory cache to store result
    - use executor service to do concurrent checks
    - status check for scheduled jobs (use quartz db)

- Logging
    - ~~use testlogger~~
    - ~~slf4j~~
    - Access and audit logging
    - Logback to config logging
    - logback add logs to file, and append

- Yatspec
    - dictionary (see shelf)
    - Use givenWhenThen from TestState (Move to another test, with different way of asserting on output using capturedInputsAndOutputs)
        - for learning test with webserver
    - ~~interesting givens~~
    - sequence diagram
    - interestingGivens.getType
    - plant uml for acceptance test
    - separate acceptance test  and end to end tests into module
    - thens and whens methods, test state

- Use third party weather service
    - https://fcc-weather-api.glitch.me/api/current?lat=51.4700&lon=0.4543
    - https://www.metaweather.com/api/
    - http client
    - marshalling request & unmarshalling response (json)
    - wiremock
    - logging
    - timeout
    - traceyid
    - audit, store weather in db
    - cache by storing result in db, update every hour

- ~~find bugs Static analysis via maven~~
    - mvn findbugs:findbugs
        - issues with findbugs???
    - mvn org.apache.maven.plugins:maven-pmd-plugin:3.6:pmd

- ~~Code coverage in build~~
   - see application/target/site/jacoco/index.html

- scheduler to check weather, and store in cache (Db - redis)
    - cache for weather service (in memory)
    - quartz, cron
    
- disaster recovery
    - Fix issues that went wrong
        - data fixes
        - end point to fix issue 
            - wrong plane status in db
            - plane should not be in db if taken off

- data warehouse views to build reports
    - sql queries to report on status of airport

- end point for /planes/{A1009} to return status of specific plane
    - use jersey
    - 
    
- dockerise
    - use maven to dockerise , fabric8
    - run acceptance tests through image
    - start script and properties

- Batch job of emails to send
    - end of hour send report by email, get details from

- metrics end point
    - prometheues

- jenkins ci build
    - ci build, run tests
    - deploy to test
    - deploy to prod

- Use Akka to make calls to db async

- extract service to separate app (database & weather service)
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
    - access api
    - display controls and return values
    - javafx
    - html/js
    - react

- implement using spring, other libraries

