package com.travelus.backend.repositories;

import com.travelus.backend.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByInviteCode(String inviteCode);
}
