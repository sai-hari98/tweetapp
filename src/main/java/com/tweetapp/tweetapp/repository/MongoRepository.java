package com.tweetapp.tweetapp.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.tweetapp.tweetapp.beans.Tweet;
import com.tweetapp.tweetapp.beans.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MongoRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public User findUserByUserName(String userName){
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(userName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userName = :val1").withExpressionAttributeValues(eav);
        List<User> users = dynamoDBMapper.scan(User.class, scanExpression);
        return users.size() > 0 ? users.get(0) : null;
    }

    public List<User> getAllUsers(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<User> users = dynamoDBMapper.scan(User.class, scanExpression);
        return new ArrayList<>(users);
    }

    public void createUser(User user){
        dynamoDBMapper.save(user);
    }

    public List<Tweet> getAllTweets(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Tweet> tweets = dynamoDBMapper.scan(Tweet.class, scanExpression);
        /*Iterable<Tweet> tweetIterable = tweetRepository.findAll();
        Iterator<Tweet> tweetIterator = tweetIterable.iterator();
        while(tweetIterator.hasNext()){
            tweets.add(tweetIterator.next());
        }*/
        return new ArrayList<>(tweets);
    }

    public List<Tweet> getUserTweets(String userName){
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(userName));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("userName = :val1").withExpressionAttributeValues(eav);
        return dynamoDBMapper.scan(Tweet.class, scanExpression);
    }

    public Tweet getTweet(String id){
        Tweet tweet = dynamoDBMapper.load(Tweet.class, id);
        /*Optional<Tweet> tweetOpt = tweetRepository.findById(id);
        if(tweetOpt.isPresent()){
            tweet = tweetOpt.get();
        }*/
        return tweet;
    }

    public void updateTweet(Tweet tweet){
        /*Query query = new Query(Criteria.where("id").is(tweet.getId()));
        mongoTemplate.findAndReplace(query, tweet, "tweets");*/
        /*tweetRepository.save(tweet);*/
        dynamoDBMapper.save(tweet, new DynamoDBSaveExpression()
            .withExpectedEntry("tweet_id",
                    new ExpectedAttributeValue(
                            new AttributeValue().withS(tweet.getId())
                    )));
    }
    
    public void updateUser(User user){
        /*Query query = new Query(Criteria.where("id").is(tweet.getId()));
        mongoTemplate.findAndReplace(query, tweet, "tweets");*/
        /*tweetRepository.save(tweet);*/
        dynamoDBMapper.save(user, new DynamoDBSaveExpression()
            .withExpectedEntry("user_id",
                    new ExpectedAttributeValue(
                            new AttributeValue().withS(user.getId())
                    )));
    }

    public void insertTweet(Tweet tweet){
        /*tweetRepository.save(tweet);*/
        dynamoDBMapper.save(tweet);
    }

    public boolean updateTweetContent(String userName, String updatedContent, String id){
        boolean updateSuccess = false;
        Tweet tweetToUpdate = getTweet(id);
        if(tweetToUpdate != null){
            tweetToUpdate.setContent(updatedContent);
            updateTweet(tweetToUpdate);
            updateSuccess = true;
        }
        return updateSuccess;
    }

    public boolean deleteTweet(String userName, String id){
        try{
            Tweet tweet = dynamoDBMapper.load(Tweet.class, id);
            dynamoDBMapper.delete(tweet);
            return true;
        }catch(Exception exception){
            log.error("Error while deleting record in dynamodb: {}",exception.getMessage());
            return false;
        }
    }

    public List<User> findUsersBySearchText(String searchParam){
        List<User> users = getAllUsers();
        return new ArrayList<>(users.stream().filter(user -> {
            return user.getUserName().contains(searchParam)
                    || user.getFirstName().contains(searchParam)
                    || user.getLastName().contains(searchParam);
        }).collect(Collectors.toList()));
    }

}
