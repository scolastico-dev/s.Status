FROM adoptopenjdk/openjdk12:latest
COPY status-shadow.jar /usr/src/app
WORKDIR /usr/src/app
CMD ["java", "--jar", "status-shadow.jar"]
