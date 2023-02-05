package com.friend.your.vprojecte.enums;

public enum AddRequestTypes {
    FRIEND(0),
    GROUP_MEMBER(1);

    final int numericValue;

    AddRequestTypes(int numericValue) {
        this.numericValue = numericValue;
    }
}
