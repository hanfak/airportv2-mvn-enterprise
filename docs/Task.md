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
    - ~~domain-enforcer libray~~
    - domain objects - immutable
    - dependency inversion for inner layers to communicate with outer layers
    - use case - shows flow of single application action

- Multiple maven modules
    - ~~app~~
    - ~~flyway~~
    - acceptance, end to end, inyegration tests
    - learning

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
    - properties for docker
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
    - acceptance tests are run via server then docker container
    - post req for takeoff and landing
    - get req for plane status
    - timeouts
    - respond with xml, custom marshaller and unmarshaller
    - ~~ready page~~
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
    - Access logging 
    - Audit logging
        - ~~audit incoming and outgoing requsts to app~~
        - audit 3rd party req/resp logs
    - Logback to config logging
    - logback add logs to file, and append
    - What to log
        - input validation failures
        - sensitive information
        - access to app, ip add and routes
        - stack traces/errors
        - actions taken (or use event sourcing)
    - Dont log
        - passwords - hash them instead
        - session id

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
    
- Feature toggle
    - For either weather service via cache or web, or other

- ~~find bugs Static analysis via maven~~
    - mvn findbugs:findbugs
        - issues with findbugs???
    - mvn org.apache.maven.plugins:maven-pmd-plugin:3.6:pmd

- ~~Code coverage in build~~
   - see application/target/site/jacoco/index.html

- scheduler to check weather, and store in cache (Db - redis)
    - cache for weather service (in memory)
    - quartz, cron 
    - https://examples.javacodegeeks.com/enterprise-java/quartz/java-quartz-with-mysql-example/

- end point for /planes/{A1009} to return status of specific plane
    - use jersey
    
- https tls
    - jks
    - keytool 
    - openssl
    - https://stackoverflow.com/questions/14362245/programmatically-configure-ssl-for-jetty-9-embedded
    - https://wiki.eclipse.org/Jetty/Howto/Configure_SSL
    
- secruity measures
    - ~~404 page~~
    - sanitise data ie PlaneId if less length add charctrs
    - ~~Use json validator when handling request body from entrypoint and services (ie at webservice level)~~
        - https://github.com/everit-org/json-schema
        - done manually
    - Throttle ip for request flooding
        - https://github.com/vladimir-bukhtoyarov/bucket4j 
        - https://www.eclipse.org/jetty/documentation/current/dos-filter.html
    - Store secrets ie database passwrods, in environment variable and access them via property files using maven or other
    - ~~sql injection- paramtised querie~~
    - https
        - use serlvet filter to enforce https apart for localhost etc
        - https://github.com/jamesward/springmvc-https-enforcer/blob/master/src/main/java/com/jamesward/HttpsEnforcer.java
    - robust error checking, log exceptions
    - ~~sanitise input~~ Done for land plane, for json and request content
    - suppress end points from 404 page
    - no secure data in logs
    - logs cannot be accessed by user
    - Only allowed users can access bash in running docker container
   
- Performance
    - get rid of regex, cache it 
    - ~~Optimise equals and Hash~~
    - In running jar , set the memory limits
    - https://blog.jooq.org/2015/02/05/top-10-easy-performance-optimisations-in-java/
    
- dockerise
    - use maven to dockerise , fabric8
    - run acceptance tests through image
    - start script and properties

- data warehouse views to build reports
    - sql queries to report on status of airport

- disaster recovery
    - Fix issues that went wrong
        - data fixes
        - end point to fix issue 
            - wrong plane status in db
            - plane should not be in db if taken off
            
- metrics end point
    - prometheues
    - https://hellokoding.com/java-application-health-check-with-prometheus-grafana-mysql-and-docker-compose/
    - https://github.com/RobustPerception/java_examples/tree/master/java_simple
    - metrics 
        - https://metrics.dropwizard.io/3.1.0/getting-started/
        - https://www.baeldung.com/dropwizard-metrics
        - Java metrics
        - db metrics
            - https://github.com/brettwooldridge/HikariCP/wiki/Dropwizard-Metrics
        - jetty metrics
            - https://metrics.dropwizard.io/3.1.0/manual/jetty/

- Batch job of emails to send
    - end of hour send report by email, get details from

- jenkins ci build
    - ci build, run tests
    - deploy to test
    - deploy to prod

- extract service to  separate app (database & weather service)
    - use docker
    - kubernetes, multiple replicas
        - pre install hook for migrating and populating db
    - encrypt password, decrypt secrets
    - traceyId
    - wiremock
    - contract tests

- Use Akka to make calls to db async

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

