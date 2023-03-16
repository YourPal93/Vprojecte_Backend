package com.friend.your.vprojecte.vprojecteutils.enums;

public enum KafkaGroups {
    VPROJECTE_MAIN("vprojecteMain");

    private String groupName;

    KafkaGroups(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
