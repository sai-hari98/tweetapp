package com.tweetapp.tweetapp.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TweetDto {
    private String id;
    private String userName;
    private String content;
    private Date timeStamp;
    private String timeAgo;
    private List<String> likedBy;
    private boolean userLiked;
    private List<String> hashTags;
    private List<ReplyDto> replies;
}
