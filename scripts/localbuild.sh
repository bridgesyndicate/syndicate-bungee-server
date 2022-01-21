#!/usr/bin/env bash
set -e

IMAGE=595508394202.dkr.ecr.us-west-2.amazonaws.com/syn-bungee-servers
TAG=local

if [[ -z $1 ]]
then
    echo "doing the build"
    rm -rf tmp
    ./scripts/localbuild.rb | grep -v 'aws ecr' | bash
    rm -f plugins.tar
    docker build -t $IMAGE:$TAG -f scripts/Dockerfile.local .

else
    echo "not building. just running"
fi
docker-compose up
