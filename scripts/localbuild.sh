#!/usr/bin/env bash
set -e
if [[ -z $1 ]]
then
    echo "doing the build"
    rm -rf tmp
    ./scripts/localbuild.rb | grep -v 'aws ecr' | bash
    rm -f plugins.tar
else
    echo "not building. just running"
fi
docker run \
       -e AWS_REGION=us-west-2 \
       -e AWS_ACCESS_KEY_ID=$DEV_AWS_ACCESS_KEY_ID \
       -e AWS_SECRET_KEY=$DEV_AWS_SECRET_KEY \
       -e SYNDICATE_ENV=development \
       -p 25565:25565 \
       --add-host=host.docker.internal:host-gateway \
       -it \
       595508394202.dkr.ecr.us-west-2.amazonaws.com/syn-bungee-servers:latest
