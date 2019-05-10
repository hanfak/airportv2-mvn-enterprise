# Domain/Entity Layer

- Domain objects
- Validation on creation if necessary
- Can be used through application
- Depend on nothing outside of package
- Conform to business language used through business or application
- Use of tiny types instead of String
- can have methods or just be a data structure
- No frameworks

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

# Infrastructure

- All the lower level details
- Can use frameworks here
- All libraries/frameworks are isolated here to make changes easier
- Hides all the details of how the database gets or stores information for example
- Have multiple implementations setup here, can be used by use cases with them knowing what it is using
- Any objects that used specifically for the framework, needs to be defined here (ie hibernate objects)

# Wiring & Configuration

- Entry point of application, main method
- Where the classes are instantiated
- Can use Spring boot here
