package com.travelus.backend.repositories;

import com.travelus.backend.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByGroupId(Long groupId);
}