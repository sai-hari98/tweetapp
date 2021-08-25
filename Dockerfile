FROM openjdk:11
EXPOSE 8080
ENTRYPOINT ["java", "-jar" "build/libs/tweetapp.jar"]