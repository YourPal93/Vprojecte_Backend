package com.friend.your.vprojecteapiserver.config;

import com.friend.your.vprojecte.vprojecteutils.enums.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(KafkaTopics.NEW_USER.getTopicName())
                .partitions(1)
                .replicas(1)
                .build();
    }
}
