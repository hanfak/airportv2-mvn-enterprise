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
 
 - status page/ health checks
    - https://filippobuletto.github.io/prometheus-healthchecks/#custom-collector
    - https://dzone.com/articles/monitoring-your-java-services-with-dropwizard-health-checks
    - https://github.com/Netflix/runtime-health
    - https://hellokoding.com/java-application-health-check-with-prometheus-grafana-mysql-and-docker-compose/
    - Manual
    - Contents
        - Application version
        - Database is up
        - 3rd party app is up
        - Https cert expiry
        - scheduled jobs fired
 
Metrics

- Useful information from jvm and db, which can be scraped by a monitoring application
- 


- https://jaxenter.com/monitoring-microservices-health-checks-142305.html
- https://microservices.io/patterns/observability/health-check-api.html
- https://dzone.com/articles/monitoring-your-java-services-with-dropwizard-health-checks