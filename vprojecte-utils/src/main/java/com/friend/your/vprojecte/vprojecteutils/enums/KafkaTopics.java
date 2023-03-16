package com.friend.your.vprojecte.vprojecteutils.enums;

public enum KafkaTopics {
    NEW_USER("newUser");

    private String topicName;

    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
