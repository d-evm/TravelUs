package com.travelus.backend.repositories;


import com.travelus.backend.models.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByGroupId(Long groupId);
}