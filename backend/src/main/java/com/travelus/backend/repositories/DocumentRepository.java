package com.travelus.backend.repositories;

import com.travelus.backend.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByGroupId(Long groupId);

    List<Document> findByGroupIdOrderByUploadedAtDesc (Long groupId);

    Optional<Document> findByIdAndGroupId (Long documentId, Long groupId);

    long countByGroupId (Long groupId);

    List<Document> findByGroupIdAndUploadedAtAfter(Long groupId, LocalDateTime timestamp);

}