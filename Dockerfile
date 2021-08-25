FROM openjdk:11
EXPOSE 8080
ADD build/libs/tweetapp-0.0.1.jar tweetapp.jar
ENTRYPOINT ["java", "-jar" "build/libs/tweetapp-0.0.1.jar"]