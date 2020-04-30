FROM adoptopenjdk/openjdk11:alpine-jre

RUN apk add --no-cache ffmpeg

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar