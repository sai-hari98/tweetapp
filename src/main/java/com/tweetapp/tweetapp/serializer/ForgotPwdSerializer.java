package com.tweetapp.tweetapp.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.tweetapp.dto.request.ForgotPwdRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class ForgotPwdSerializer implements Serializer<ForgotPwdRequest> {

    @Override
    public byte[] serialize(String topic, ForgotPwdRequest data) {
        byte[] serializedValue = null;
        ObjectMapper objectMapper = new ObjectMapper();
        if(data != null) {
            try {
                serializedValue = objectMapper.writeValueAsString(data).getBytes();
            } catch (JsonProcessingException jsonProcessingException) {
                log.error("Error while serializing bean: {}", jsonProcessingException.getMessage());
            }
        }
        return serializedValue;
    }

}
