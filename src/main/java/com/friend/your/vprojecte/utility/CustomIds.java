package com.friend.your.vprojecte.utility;

public class CustomIds {

    public static String twoInts(int val1, int val2) {
        StringBuilder key = new StringBuilder("");
        key.append(Math.min(val1, val2)).append(",").append(Math.max(val1,val2));
        return key.toString();
    }
}
