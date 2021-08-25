package com.tweetapp.tweetapp.utils;

import com.tweetapp.tweetapp.constants.TweetAppConstants;
import com.tweetapp.tweetapp.dto.response.ErrorResponse;
import com.tweetapp.tweetapp.dto.response.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TweetAppUtils {

    public static ResponseEntity<Object> getSuccessResponse(String message, Object data){
        SuccessResponse success = new SuccessResponse();
        success.setMessage(message);
        success.setData(data);
        return new ResponseEntity<Object>(success, HttpStatus.OK);
    }

    public static ResponseEntity<Object> getErrorResponse(String message, HttpStatus httpStatus){
        ErrorResponse errResp = new ErrorResponse();
        errResp.setMessage(message);
        return new ResponseEntity<Object>(errResp, httpStatus);
    }
}
