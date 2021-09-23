package com.tweetapp.tweetapp.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.tweetapp.tweetapp.domain.UserDomain;
import com.tweetapp.tweetapp.dto.request.ForgotPwdRequest;

@Slf4j
@Component
public class ForgotPwdListener {

    private static final String TOPIC = "topic.forgotpwd";

    @Autowired
    private UserDomain userDomain;

    @KafkaListener(topics = TOPIC)
    public void messageListener(ConsumerRecord<String, ForgotPwdRequest> consumerRecord, Acknowledgment ack) {

        String key = consumerRecord.key();
        ForgotPwdRequest value = consumerRecord.value();

        log.info("Received msg with key: {}, value: {}",key, value);
        if(value != null){
        	userDomain.updateForgottedPwd(value);
        }

        ack.acknowledge();
    }
}
