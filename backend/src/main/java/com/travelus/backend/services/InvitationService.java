package com.travelus.backend.services;

public interface InvitationService {
    String createInvitation(Long groupId, String inviterUsername);
    void acceptInvitation(String inviteCode, String currentUsername);
}
