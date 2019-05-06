# Unit Tests

Testing using TDD

Mocking all dependencies and instantiating only the class under test

Very fast, ok to have lots of these

Should test exceptions, edge cases

Should not test functionality/behaviour which have already been covered by :

    - acceptance tests
    - integration tests
    - end to end tests
    
as this will make code more brittle, (multiple test code to change when changing production code)