Partial Flows:

Landing

2. Check if playing is flying
    * false exit
3. Check if at airport
    * true exit
4. land plane
    * status changes
5. Add plane to airport

Take off

2. Check if playing is landed
    * false exit
3. Check if at airport
    * false exit
4. take off plane
    * status change
6. Remove plane from aiport


Full Flows:

Landing

1. Check weather is good
    * false exit
2. Check if playing is flying
    * false exit
3. Check if at airport
    * true exit
    * Check correct gate is available
4. land plane
    * status changes
5. Add plane to airport
6. Add plane to gate

Take off

1. Check weather is good
    * false exit
2. Check if playing is landed
    * false exit
3. Check if at airport
    * false exit
    * Check if at any gate
        * false exit
4. take off plane
    * status change
5. Remove plane from gate
6. Remove plane from aiport