# Perform the extraction in a separate builder container
FROM eclipse-temurin:21.0.7_6-jre-alpine AS builder
WORKDIR /builder
# This points to the built jar file in the target folder
# Adjust this to 'build/libs/*.jar' if you're using Gradle
ARG JAR_FILE=target/flavormate-server.jar
# Copy the jar file to the working directory and rename it to application.jar
COPY ${JAR_FILE} flavormate-server.jar
# Extract the jar file using an efficient layout
RUN java -Djarmode=tools -jar flavormate-server.jar extract --layers --destination extracted

FROM eclipse-temurin:21.0.7_6-jre-alpine
# needed for docker health command
RUN apk add --no-cache curl postgresql-client

RUN mkdir /opt/app

WORKDIR /opt/app
# Copy the extracted jar contents from the builder container into the working directory in the runtime container
# Every copy step creates a new docker layer
# This allows docker to only pull the changes it really needs
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

COPY entrypoint.sh /opt/app/entrypoint.sh

ENTRYPOINT ["sh","/opt/app/entrypoint.sh"]
