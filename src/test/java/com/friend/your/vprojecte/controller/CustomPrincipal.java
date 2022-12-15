package com.friend.your.vprojecte.controller;

import lombok.AllArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
public class CustomPrincipal implements Principal {

    private String name;

    @Override
    public String getName() {
        return name;
    }
}
