FROM 595508394202.dkr.ecr.us-west-2.amazonaws.com/syn-bungee-dist
WORKDIR /app/bungee-home/plugins
ADD ./target/syndicate-bungee-server-1.0-SNAPSHOT.jar .
WORKDIR /app/bungee-home
