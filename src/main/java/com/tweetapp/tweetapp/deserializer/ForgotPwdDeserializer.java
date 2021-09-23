package com.tweetapp.tweetapp.deserializer;

import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.tweetapp.dto.request.ForgotPwdRequest;

public class ForgotPwdDeserializer implements Deserializer<ForgotPwdRequest> {
    @Override
    public ForgotPwdRequest deserialize(String topic, byte[] data) {

        ForgotPwdRequest message = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(data, ForgotPwdRequest.class);
        } catch (IOException e) {
        }
        return message;
    }
}