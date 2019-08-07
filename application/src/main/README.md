# Clean architecture

* This application follows the guidelines of a clean architecture. 
* The aim is to separate concerns, by dividing the software into layers
* This helps keep business/core rules/logic independent
    * framework independent
    * Testable
    * UI Independent (Web server or console)
    * Database Independent
    * 3rd party or internal api independent 
* The Inner layers know nothing of the outer layers
    * For this app the it follows from inner to outer layer: 
        * Core layer
            * Domain/Entity
            * Usecases/Application
        * Outer layer
            * Infrastructure
            * Wiring & configuration
    * The inner layers communicate with outer layers via interfaces (ports) using polymorphism and dependency inversion
    * Inner layer should not be affected by changes in the outer layers
  
    


# Domain/Entity Layer

- Domain objects
    - can validate, normalise, santise data
- Are immutable, no setters, fields are final
    - helps with concurrency
- Validation on creation if necessary
    - Avoid using primitive types or inbuilt library types
- The least likely to change
- Encapsulate the business rules
- Can be used through application
- Depend on nothing outside of package
- Conform to business language used through business or application
- Use of tiny types instead of String
- can have methods or just be a data structure
- No frameworks
- Improve readability of code
- No confusing when passing arguments (when primitives) to method or constructor, as they must be wrapped in a type

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
- interface adapters (ports)
    - Allow use case to communicate with database, third party api etc
    - change data from outer layer to inner layer, that is most convienent for inner layer

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
