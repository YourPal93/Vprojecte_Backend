package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppTokenRepository extends JpaRepository<AppToken, Integer> {

    Optional<AppToken> findByTokenCode(String tokenCode);
}
