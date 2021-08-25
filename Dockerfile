FROM openjdk:11
EXPOSE 8080
ADD build/libs/tweetapp.jar tweetapp.jar
ENTRYPOINT ["java", "-jar" "build/libs/tweetapp.jar"]