#!/bin/bash

#docker logout
docker login

docker-compose down
docker build --cache-from mintosbackend . -t mintosbackend:latest
docker tag mintosbackend:latest ansism/mintosbackend:latest
docker push ansism/mintosbackend:latest
#docker-compose up


