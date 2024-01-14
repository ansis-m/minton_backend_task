#!/bin/bash
#
docker logout
docker login

#docker-compose down
docker build --no-cache -t frontend:latest .
docker tag frontend:latest ansism/frontend:latest
docker push ansism/frontend:latest
#docker-compose up


