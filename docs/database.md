mkdir -p $HOME/docker/volumes/postgres
docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres
docker exec -it postgres bash
psql -h localhost -U postgres -d postgres or psql -h localhost -U postgres -d postgres -W




links:

- https://hub.docker.com/_/postgres
- To set up docker db https://hackernoon.com/dont-install-postgres-docker-pull-postgres-bee20e200198
- https://stackoverflow.com/questions/37694987/connecting-to-postgresql-in-a-docker-container-from-outside
- https://blog.codecentric.de/en/2017/01/flyway-tutorial-managing-database-migrations/
- http://pointbeing.net/weblog/2008/03/mysql-versus-postgresql-adding-a-last-modified-column-to-a-table.html