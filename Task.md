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
- Add Jetty server, split up usecase into several classes
    - property file
    - respond with json, custom marshaller and unmarshaller
    - acceptance tests are run via server then docker container
    - post req for takeoff and landing
    - get req for plane status
    - logging
    - log incoming and outgoing requests

- Add db, object pooling, property file
    - mysql/postgres
    - c3po or hikari pooling
    - use in memory db ie H2 and properties  and code (extra methods on db) just for tests
    - use stub for database in test
- flyway db to setup database
    - maven, module
- put logging behind interface instead of calling library directly
    - use testlogger
- Yatspec
    - dictionary
    - interestingGivens.getType
    - plant uml for acceptance test
- Use third party weather service, use wire mock for stub, http client to talk wiht it
    - https://fcc-weather-api.glitch.me/api/current?lat=51.4700&lon=0.4543
    - https://www.metaweather.com/api/
    - logging
    - timeout
- separate acceptance test into module
- find bugs Static analysis via maven
- scheduler to check weather, and store in cache (Db - redis)
    - quartz, cron
- status page, check db & weather service is up, scheduled job every minute
- Wrap logger instead using it directly
- Use Akka to make calls to db async
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
    -
- cqrs and event sourcing, aggregates for flow
- implement using spring, other libraries
