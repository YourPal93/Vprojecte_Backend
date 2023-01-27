package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.entity.Chat;
import com.friend.your.vprojecte.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {


}
