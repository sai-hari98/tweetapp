package com.tweetapp.tweetapp.dto.response;

import com.tweetapp.tweetapp.dto.BaseDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TweetsResponse extends BaseDto {

    private List<TweetDto> tweets = new ArrayList<>();

}
