package com.friend.your.vprojecte.service.impl;

import com.friend.your.vprojecte.dao.AppTokenRepository;
import com.friend.your.vprojecte.dao.InactiveAccountRepository;
import com.friend.your.vprojecte.dto.AppUserDto;
import com.friend.your.vprojecte.entity.AppToken;
import com.friend.your.vprojecte.entity.InactiveAccount;
import com.friend.your.vprojecte.service.RegistrationService;
import com.friend.your.vprojecte.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final MailServiceImpl mailService;
    private final UserService userService;
    private final AppTokenRepository tokenRepository;
    private final InactiveAccountRepository inactiveAccountRepository;


    @Override
    public boolean tokenTaken(String userLogin, String userEmail) {
        if(inactiveAccountRepository.existsByLogin(userLogin)) return true;

        return inactiveAccountRepository.existsByEmail(userEmail);
    }
    @Override
    public void register(AppUserDto userDto) {

        InactiveAccount inactiveAccount = new InactiveAccount(userDto);
        AppToken token = new AppToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusMinutes(15),
                inactiveAccount
        );

        tokenRepository.save(token);

        String link = "http://localhost:8080/api/account?token=" + token.getTokenCode();

        mailService.send(userDto.getEmail(), "Account activation", link, userDto.getName());
    }

    @Override
    public void activateAccount(String tokenCode) {
        log.info("Activating account with token code {}", tokenCode);

        AppToken token = tokenRepository.findByTokenCode(tokenCode)
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        InactiveAccount inactiveAccount = token.getTokenAccount();

        if(token.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.deleteById(token.getId());
            throw new RuntimeException("Token has expired");
        }

        AppUserDto userDto = new AppUserDto();

        userDto.setName(inactiveAccount.getName());
        userDto.setPassword(inactiveAccount.getPassword());
        userDto.setLogin(inactiveAccount.getLogin());
        userDto.setEmail(inactiveAccount.getEmail());
        userDto.setBirthdate(inactiveAccount.getBirthdate());

        tokenRepository.delete(token);
        inactiveAccountRepository.delete(inactiveAccount);

        userService.saveUser(userDto);
    }
}
