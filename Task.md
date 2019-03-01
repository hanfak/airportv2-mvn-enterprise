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


Technical TODO:

- Have an application start up(main, property file for prod)
    - configuration, wiring
- Add Exceptions, then try catch them and add logs with stack trace
- Add Jetty server, split up usecase into several classes
    - respond with json
    - acceptance tests are run via server then docker container
    - post req for takeoff and landing
    - get req for plane status
- Add db, object pooling, property file
- flyway db to setup database
- use stub for database in test
- plant uml for acceptance test
- Use third party weather service, use wire mock for stub, http client to talk wiht it
    - https://fcc-weather-api.glitch.me/api/current?lat=51.4700&lon=0.4543
    - https://www.metaweather.com/api/
- log incoming and outgoing requests
- separate acceptance test into module
- cqrs and event sourcing, aggregates for flow
- find bugs Static analysis via maven
- scheduler to check weather, and store in cache (Db - redis)
- status page, check db & weather service is up, scheduled job every minute
- dockerise, use maven to dockerise and run acceptance tests through image
- jenkins ci build
- metrics end point, prometheues
