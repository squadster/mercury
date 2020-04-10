FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=release","--add-opens=java.base/jdk.internal.loader=ALL-UNNAMED","-jar","/app.jar"]