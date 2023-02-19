package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.AppUserPlate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPlateRepository extends JpaRepository<AppUserPlate, Integer> {

    Optional<AppUserPlate> findByUserLogin(String login);

    Page<AppUserPlate> findAll(Pageable pageable);

    Page<AppUserPlate> findByUserLoginContaining(String login , Pageable pageable);

    boolean existsByUserLogin(String userLogin);
}
