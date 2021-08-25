package com.tweetapp.tweetapp.dto.response;

import com.tweetapp.tweetapp.dto.BaseDto;
import lombok.Data;

@Data
public class LoginResponse extends BaseDto {
    private String userName;
    private String jwt;
}
