package com.travelus.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Group group;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
