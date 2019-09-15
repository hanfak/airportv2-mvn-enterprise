#!/bin/bash

mkdir -p $HOME/docker/volumes/postgres;
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres:11;
cd ../flyway-sql;
mvn clean compile flyway:migrate;
