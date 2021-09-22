FROM openjdk:8-jdk-slim-buster
WORKDIR /app
ADD bungee-home.tar.xz .
WORKDIR /app/bungee-home
CMD ["java", "-jar", "-DIReallyKnowWhatIAmDoingISwear=true", "BungeeCord.jar", "nogui"]
