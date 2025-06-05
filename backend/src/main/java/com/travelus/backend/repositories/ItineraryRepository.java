package com.travelus.backend.repositories;


import com.travelus.backend.models.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByGroupId(Long groupId);

    List<Itinerary> findByGroupIdOrderByStartDateAsc(Long groupId);

    @Query("SELECT i FROM Itinerary i WHERE i.group.id = :groupId " +
            "AND i.startDate >= :startDate AND i.endDate <= :endDate " +
            "ORDER BY i.startDate ASC")
    List<Itinerary> findByGroupIdAndDateRange (@Param("groupId") Long groupId,
                                               @Param("startDate")LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT i FROM Itinerary i WHERE i.group.id = :groupId " +
            "AND i.startDate >= :currentDate ORDER BY i.startDate ASC")
    List<Itinerary> findUpcomingByGroupId (@Param("groupId") Long groupId,
                                           @Param("currentDate") LocalDateTime currentDate);

    long countByGroupId(Long groupId);
}