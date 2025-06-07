package com.travelus.backend.repositories;

import com.travelus.backend.models.Group;
import com.travelus.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByGroupOrderByTimestampAsc(Group group);
}