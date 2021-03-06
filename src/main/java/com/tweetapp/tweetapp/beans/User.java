package com.tweetapp.tweetapp.beans;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

//@Document(collection = "users")
@DynamoDBTable(tableName = "users")
@Data
public class User {
    @DynamoDBHashKey(attributeName = "user_id")
    private String id;
//    @Field("userName")
    @DynamoDBAttribute(attributeName = "userName")
    private String userName;
//    @Field("pwd")
    @DynamoDBAttribute(attributeName = "pwd")
    private String pwd;
//    @Field("firstName")
    @DynamoDBAttribute(attributeName = "firstName")
    private String firstName;
//    @Field("lastName")
    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;
//    @Field("email")
    @DynamoDBAttribute(attributeName = "email")
    private String email;
//    @Field("phno")
    @DynamoDBAttribute(attributeName = "phno")
    private String phno;
}
