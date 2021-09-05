FROM openjdk:11
EXPOSE 8080
COPY build/libs/tweetapp-0.0.1.jar tweetapp.jar
CMD ["java", "-jar" ,"tweetapp.jar"]