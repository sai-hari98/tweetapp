package com.tweetapp.tweetapp.dto.response;

import com.tweetapp.tweetapp.dto.BaseDto;
import lombok.Data;

@Data
public class SuccessResponse extends BaseDto {
    private boolean isSuccess = true;
    private String message;
    private Object data;
}
