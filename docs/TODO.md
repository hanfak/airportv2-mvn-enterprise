git commit message

Addressing todos
- validate fields in domain object before assigning to fields
- More descriptive factory methods, where null can be placed here instead of in usecase
- tried spliting planeInventoryClass, using ISP, but too much duplication
- Extracted formating of req/resp in logging for client into a class, allowed me to simplify tests


- implement weather service
    - ~~use stub to return true or false~~ used in acceptance test
    - ~~implement service in infrastructure to return random weather~~
    - ~~use wiremock to return actual response from weather service, for integration test~~
    - ~~implement client to get weather from service~~
    - ~~health check for endpoint~~
    - ~~Probes run concurrently~~
    - ~~Deal with bad response from api~~
    - ~~sequence diagrams for exteranl calls~~
    - use captured inputs, instead of fields for assertions
    - ~~sequence diagram for yatspec include internal api calls~~
        - ~~use wiremock~~
- ~~audit logs for internal calls~~
- ~~refactor landusecase~~
- TODO
- database/jdbc - use factory to new up jdbc class
- domain objects used in outer layers, should be different
    - better mapping
- input to usecase, validate via interface inner class which is implemeted by use case
    = not null validation on usecase input object fields
- Split use case into ports
- improve maven pom
    - profiles
        - activations
    - skip tests 
    - depenednecy management
        - organise in groups for test and prod scope
    - multiple excutions for test packages in surefire and failsafe
    - profile for local dev to speed up build
    - modules for ports, infrastructure etc
    - Add pitest
- user story 3        
- Use story 5 and 6
- Handle jsonprocessing exception as runtime, what to do?
- Split learning into separate module
- Add a lock to the plane (When splitting up)
    - add to readme 
        - Can only do synchronous calls
        - issues with making multiple calls at same time or during processesing on the same plane 
- sanitise plane object
- check weather, and store in cache
    - cache, backed by db
    - use time eviction to remove data and call weather
    - https://www.baeldung.com/java-caching-caffeine
- scheduler to call the info endpoint (new status page like), to list all planes and storage amount
    - use quartz backed by db
    - end point can be called by user of app
    
- Yatspec output index page
    - Yatspec - use state extractor for thens
    - yatspec - extract whens to another class, use test state
- 404 page with links for all endpoints
- Use error codes for sad paths
- refactor repository class, split operations into separate classe, use factory like shopofhan
- organise wiring, split out jdbc, more singletons, set up for test
- dockerise, fabric8, porperties - load properties to fixed location (property loader)
    - When running end to end test, bring up docker image of application with test properties
- https port
- split acceptance tests to another module
- grafana script to show metrics in dashboard
- Fix intellij to compile tests
- CI build
    - jenkins
- Deploy on minikube
    - helm, kubernetes, service, configmap, secrets, ingress, egress

New apps

- Find a way to add a scheduled job(quartz) into the app
    - Schedule job for accessing airport is full
- Find a way to do batching multiple to do later jobs
- Find a way to use a message broker/ queue
- pact tests & tracey id when splitting weather service into separate app
    - TraceyId logs, and adding to headers in req/resp via servlet filter and http client
