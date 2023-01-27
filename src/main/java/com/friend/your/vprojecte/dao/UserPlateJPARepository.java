package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.AppUserPlate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPlateJPARepository extends JpaRepository<AppUserPlate, Integer> {

    Optional<AppUserPlate> findByLogin(String login);

    Page<AppUserPlate> findAll(Pageable pageable);

    Page<AppUserPlate> findByLoginContaining(String login , Pageable pageable);
}
