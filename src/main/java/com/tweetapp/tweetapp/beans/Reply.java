package com.tweetapp.tweetapp.beans;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Data
public class Reply {

    private String userName;
    @Min(value = 1, message = "Content should not be empty")
    @Max(value = 144, message = "Content should be of max 144 characters")
    private String content;
    private Date timeStamp;
    /*private int noOfLikes;
    private boolean userLiked;*/
    private List<String> hashTags;
}
