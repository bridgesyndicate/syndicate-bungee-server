#!/bin/bash
set -ve
VERSION=01
echo version $VERSION
echo building with the contents of bungee-home
echo edit if you don\'t have this directory
tar -cJf bungee-home.${VERSION}.tar.xz bungee-home/
aws s3 cp bungee-home.${VERSION}.tar.xz s3://syndicate-minecraft-artifacts/
mv bungee-home.${VERSION}.tar.xz bungee-home.tar.xz
# aws s3 cp s3://syndicate-minecraft-artifacts/bungee-home.${VERSION}.tar.xz bungee-home.tar.xz
REPOSITORY_HOST=595508394202.dkr.ecr.us-west-2.amazonaws.com
REPOSITORY_URI=$REPOSITORY_HOST/syn-bungee-dist
aws ecr get-login-password  | docker login --username AWS --password-stdin $REPOSITORY_HOST
docker build -t $REPOSITORY_URI:latest .
docker push $REPOSITORY_URI:latest
rm bungee-home.tar.xz
