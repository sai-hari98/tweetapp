package com.tweetapp.tweetapp.dto.response;

import com.tweetapp.tweetapp.dto.BaseDto;
import lombok.Data;

@Data
public class ErrorResponse extends BaseDto {
    private boolean isSuccess = false;
    private String type = "ERROR";
    private String code;
    private String message;
}
