package com.friend.your.vprojecte.controller;

import com.friend.your.vprojecte.entity.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.security.Principal;

@AllArgsConstructor
public class CustomRequestPostProcessor implements RequestPostProcessor {

    private AppUser user;
    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        request.setUserPrincipal(new CustomPrincipal(user.getLogin()));
        return request;
    }
}
