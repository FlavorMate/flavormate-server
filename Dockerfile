FROM eclipse-temurin:21-jre-alpine

# needed for docker health command
RUN apk add --no-cache curl postgresql-client

RUN mkdir /opt/app

COPY entrypoint.sh /opt/app/entrypoint.sh
COPY target/flavormate-server.jar /opt/app/flavormate-server.jar

ENTRYPOINT ["sh","/opt/app/entrypoint.sh"]
