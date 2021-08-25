package com.tweetapp.tweetapp.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.tweetapp.tweetapp.constants.TweetAppConstants;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignupRequest {
    @Min(value = 5, message = TweetAppConstants.UNAME_MIN_MSG)
    @JsonAlias("username")
    private String userName;
    @Min(value = 3, message = "First name should be minimum 3 characters")
    @JsonAlias("fname")
    private String firstName;
    @Min(value = 3, message = "Last name should be minimum 3 characters")
    @JsonAlias("lname")
    private String lastName;
    @Email(message = "Email is invalid")
    private String email;
    @Min(value = 8, message = TweetAppConstants.PWD_MIN_MSG)
    @Max(value = 16, message = TweetAppConstants.PWD_MAX_MSG)
    private String pwd;
    @Pattern(regexp = "[0-9]{10}", message = "Mobile Number should be a 10 digit number")
    private String phno;
}
