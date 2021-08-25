package com.tweetapp.tweetapp.producer;

import com.tweetapp.tweetapp.dto.request.ForgotPwdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ForgotPwdProducer {

    private static final String TOPIC = "topic.forgotpwd";

    @Autowired
    private KafkaTemplate<String, ForgotPwdRequest> kafkaTemplate;

    public void publishForgotPwdReq(String reqId, ForgotPwdRequest forgotPwdRequest){
        kafkaTemplate.send(TOPIC, reqId, forgotPwdRequest);
    }

}
