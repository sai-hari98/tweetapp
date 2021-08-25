package com.tweetapp.tweetapp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class TweetAppConfig {

    @Value("${jwt.secretkey}")
    private String signingKey;

}
