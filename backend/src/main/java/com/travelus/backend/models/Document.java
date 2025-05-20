package com.travelus.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Group group;

    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String url;

    @ManyToOne
    private User uploadedBy;

    private LocalDateTime uploadedAt = LocalDateTime.now();
}
