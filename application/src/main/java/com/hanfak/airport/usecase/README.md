# Usecases/Application Layer

- Pure business logic, application specific business rules
- Uses domain objects
- Uses and defines interfaces, to allow it use infrastructure/third party/database etc
    - Dependency inversion
    - Not affected by anything infrastructure (database changes etc)
- Only changes when business logic changes
- use cases orchestrate the flow of data to and from the entities
- use cases direct the domain/entities to achieve their goals
- Plain old java, java libraries, 
    - maybe some simple libraries to make understanding easy or reducing complexity of code(ie functional library) 
        - But be careful if library goes out of date
    - no frameworks
- Single method interfaces (interface segration/ command pattern)
- interface adapters
    - Allow use case to communicate with database, third party api etc