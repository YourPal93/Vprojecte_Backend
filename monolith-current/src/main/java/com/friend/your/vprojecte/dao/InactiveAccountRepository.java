package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.InactiveAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InactiveAccountRepository extends JpaRepository<InactiveAccount, Integer> {
    boolean existsByLogin(String userLogin);

    boolean existsByEmail(String userEmail);
}
