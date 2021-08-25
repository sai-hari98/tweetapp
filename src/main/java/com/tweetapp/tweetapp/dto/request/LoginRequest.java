package com.tweetapp.tweetapp.dto.request;

import com.tweetapp.tweetapp.constants.TweetAppConstants;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class LoginRequest {

    @Min(value = 5, message = TweetAppConstants.UNAME_MIN_MSG)
    private String userName;
    @Min(value = 8, message = TweetAppConstants.PWD_MIN_MSG)
    @Max(value = 16, message = TweetAppConstants.PWD_MAX_MSG)
    private String password;
}
