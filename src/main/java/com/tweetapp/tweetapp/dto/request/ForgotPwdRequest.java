package com.tweetapp.tweetapp.dto.request;

import com.tweetapp.tweetapp.constants.TweetAppConstants;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class ForgotPwdRequest {

    @Min(value = 5, message = TweetAppConstants.UNAME_MIN_MSG)
    private String userName;
    @Pattern(regexp = "[0-9]{10}", message = "Mobile Number should be a 10 digit number")
    private String phno;
    @Min(value = 8, message = TweetAppConstants.PWD_MIN_MSG)
    @Max(value = 16, message = TweetAppConstants.PWD_MAX_MSG)
    private String newPwd;

}
