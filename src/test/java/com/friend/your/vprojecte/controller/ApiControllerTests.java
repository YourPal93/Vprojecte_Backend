package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ApiController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ApiControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    private AppUser mockUser;

    @BeforeEach
    public void init() {
        mockUser = new AppUser(
                "AppUser",
                "1234",
                "Test",
                "something@proba.test",
                LocalDate.of(1111, 11, 11));
    }

    @Test
    public void ApiController_Add_ReturnCreated() throws Exception {
        given(userService.save(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockUser)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
