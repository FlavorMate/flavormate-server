FROM eclipse-temurin:21-jre-alpine
# needed for docker health command
RUN apk add --no-cache curl

RUN mkdir /opt/app

COPY target/flavormate-server.jar /opt/app/flavormate-server.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=release", "/opt/app/flavormate-server.jar"]
