package com.friend.your.vprojecte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friend.your.vprojecte.dao.UserRepository;
import com.friend.your.vprojecte.dto.AppUserCredentialsDto;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.entity.AppUser;
import com.friend.your.vprojecte.service.impl.RegistrationServiceImpl;
import com.friend.your.vprojecte.test_util.TestUserUtil;
import com.friend.your.vprojecte.test_util.dto.TestUserDtoUtil;
import com.friend.your.vprojecte.utility.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.yml")
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    @Autowired
    private ApiController underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mock
    private RegistrationServiceImpl registrationService;
    @Autowired
    private JwtUtil jwtUtil;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void register_GivenExistingUserLogin_ShouldReturnBadRequest() throws Exception {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        AppUserDto testUserDto = TestUserDtoUtil.getTestUserDtoNoId();

        userRepository.save(testUser);

        ResultActions response = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)));

        response.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User with the same login or email already exists")));
    }

    @Test
    void login_GivenInvalidUserCredentials_ShouldReturnUnauthorized() throws Exception {
        AppUser testUser = TestUserUtil.getTestUserNoId();
        AppUserCredentialsDto testUserCredentials = new AppUserCredentialsDto();

        testUserCredentials.setUsername(testUser.getLogin());
        testUserCredentials.setPassword("NONE");
        testUser.setPassword(passwordEncoder.encode(testUser.getPassword()));

        userRepository.save(testUser);

        ResultActions response = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserCredentials)));


        response.andExpect(status().isUnauthorized());

    }
}