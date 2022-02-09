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
docker-compose up
