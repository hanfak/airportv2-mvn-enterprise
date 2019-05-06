mkdir -p $HOME/docker/volumes/postgres
docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres:11
docker exec -it pg-docker bash
psql -h localhost -U postgres -d postgres -W

docker exec pg-docker psql -U postgres -c"CREATE DATABASE airportlocal;" postgres



links:

- https://hub.docker.com/_/postgres
- To set up docker db https://hackernoon.com/dont-install-postgres-docker-pull-postgres-bee20e200198
- https://stackoverflow.com/questions/37694987/connecting-to-postgresql-in-a-docker-container-from-outside
- https://blog.codecentric.de/en/2017/01/flyway-tutorial-managing-database-migrations/
- http://pointbeing.net/weblog/2008/03/mysql-versus-postgresql-adding-a-last-modified-column-to-a-table.html

Flyway

- https://flywaydb.org/getstarted/
- Need to set up docker images, as above, and create the db

- Commands (cd flyway-sql)
    - apply clean
        -  mvn compile flyway:clean
    - clean db, drop tables
        - mvn compile flyway:migrate
