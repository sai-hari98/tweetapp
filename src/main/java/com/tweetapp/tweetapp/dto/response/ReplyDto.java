package com.tweetapp.tweetapp.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReplyDto {
    private String userName;
    private String content;
    private Date timeStamp;
    private String timeAgo;
    /*private int noOfLikes;
    private boolean userLiked;*/
    private List<String> hashTags;
}
