package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.AppTokenRepository;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.entity.AppToken;
import com.friend.your.vprojecte.entity.InactiveAccount;
import com.friend.your.vprojecte.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private MailServiceImpl mailService;
    @Mock
    private AppTokenRepository tokenRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    RegistrationServiceImpl underTest;

    @Test
    void register_GivenValidUserDto_ShouldNotReturnAnything() {
        AppToken testToken = mock(AppToken.class);
        AppUserDto testUserDto = new AppUserDto();

        testUserDto.setName("");
        testUserDto.setEmail("");

        when(tokenRepository.save(any(AppToken.class))).thenReturn(testToken);
        doNothing().when(mailService).send(anyString(), anyString(), anyString(), anyString());

        underTest.register(testUserDto);

        verify(tokenRepository).save(any(AppToken.class));
        verify(mailService).send(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void activateAccount_GivenValidTokenCode_ShouldReturnAppUserDto(){
        AppToken testToken = new AppToken();
        InactiveAccount testAccount = mock(InactiveAccount.class);
        AppUserDto testUserDto = mock(AppUserDto.class);

        testToken.setId(1);
        testToken.setExpiresAt(LocalDateTime.of(2100, 1, 1, 1, 1, 1));
        testToken.setTokenAccount(testAccount);

        when(tokenRepository.findByTokenCode(anyString())).thenReturn(Optional.of(testToken));
        doNothing().when(tokenRepository).deleteById(anyInt());
        when(userService.saveUser(any(AppUserDto.class))).thenReturn(testUserDto);

        underTest.activateAccount("");

        verify(tokenRepository).findByTokenCode(anyString());
        verify(tokenRepository).deleteById(anyInt());
        verify(userService).saveUser(any(AppUserDto.class));
    }

}