package com.tweetapp.tweetapp.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.tweetapp.tweetapp.constants.TweetAppConstants;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Data
//@Document(collection = "tweets")
@DynamoDBTable(tableName = "tweets")
public class Tweet {

//    @Field(name = "_id")
    @DynamoDBHashKey(attributeName = "tweet_id")
    private String id;
    @Min(value = 5, message = TweetAppConstants.UNAME_MIN_MSG)
//    @Field(name = "userName")
    @DynamoDBAttribute(attributeName = "userName")
    private String userName;
    @Min(value = 1, message = "Content should not be empty")
    @Max(value = 144, message = "Content should be of max 144 characters")
//    @Field(name = "content")
    @DynamoDBAttribute(attributeName = "content")
    private String content;
//    @Field(name = "timeStamp")
    @DynamoDBAttribute(attributeName = "timeStamp")
    private Date timeStamp;
//    @Field(name = "likedBy")
    @DynamoDBAttribute(attributeName = "likedBy")
    private List<String> likedBy;
//    @Field(name = "hashtags")
    @DynamoDBAttribute(attributeName = "hashtags")
    private List<String> hashTags;
//    @Field(name = "replies")
    @DynamoDBAttribute(attributeName = "replies")
    private List<Reply> replies;
}
