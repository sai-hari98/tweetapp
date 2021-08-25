package com.tweetapp.tweetapp.dto.request;

import com.tweetapp.tweetapp.beans.Tweet;
import com.tweetapp.tweetapp.dto.BaseDto;
import lombok.Data;

import javax.validation.Valid;

@Data
public class TweetRequest extends BaseDto {

    @Valid
    private Tweet tweet;
    private String updatedTweetContent;

}
