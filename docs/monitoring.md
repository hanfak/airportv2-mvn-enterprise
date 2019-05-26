- All probes are run when scheduler is called, to keep upto date
- Each probe is run concurrently
- a status page endpoint is available to access the results
- Areas to monitor
    - application version
    - connection to
        - 3rd party services (change in api, service is down)
        - Internal services
        - database
        - file servers
    - Application
        - failing at task
        - task not run correctly
        - scheduled jobs not run or completed
        - high latency of SLA paths
- Different levels of status
 
 
 
Metrics

- Useful information from jvm and db, which can be scraped by a monitoring application
- 


- https://jaxenter.com/monitoring-microservices-health-checks-142305.html
- https://microservices.io/patterns/observability/health-check-api.html
- https://dzone.com/articles/monitoring-your-java-services-with-dropwizard-health-checks