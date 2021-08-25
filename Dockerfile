FROM openjdk:11

COPY . /usr/app/tweetapp
WORKDIR /usr/app/tweetapp

FROM gradle:6.8.1-jdk8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build

EXPOSE 8080

CMD ["java", "-jar" "build/libs/tweetapp.jar"]