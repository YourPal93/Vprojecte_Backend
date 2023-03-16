package com.friend.your.vprojecteapiserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.vprojecteutils.dto.UserDto;
import com.friend.your.vprojecte.vprojecteutils.enums.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(UserDto userDto) {
        String dtoAsString;

        try {
            dtoAsString = objectMapper.writeValueAsString(userDto);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", userDto, ex);
            return;
        }

        kafkaTemplate.send(KafkaTopics.NEW_USER.getTopicName(), dtoAsString);
    }
}
