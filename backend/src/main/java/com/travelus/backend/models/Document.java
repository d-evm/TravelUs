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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;

    @ManyToOne
    private Group group;

    @ManyToOne
    private User uploadedBy;

    private LocalDateTime uploadedAt;
}
