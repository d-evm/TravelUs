package com.travelus.backend.services;

import com.travelus.backend.models.Group;
import com.travelus.backend.models.GroupMember;
import com.travelus.backend.models.Invitation;
import com.travelus.backend.models.User;
import com.travelus.backend.repositories.GroupMemberRepository;
import com.travelus.backend.repositories.GroupRepository;
import com.travelus.backend.repositories.InvitationRepository;
import com.travelus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService{

    @Autowired private GroupRepository groupRepository;
    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private InvitationRepository invitationRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public String createInvitation(Long groupId, String inviterUsername){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getAdmin().getUsername().equals(inviterUsername)){
            throw new RuntimeException("Only the group admin can invite new members.");
        }

        String code = UUID.randomUUID().toString();

        Invitation invite = Invitation.builder()
                .group(group)
                .inviteCode(code)
                .createdAt(LocalDateTime.now())
                .build();

        invitationRepository.save(invite);

        return "http://localhost:8084/groups/join?code=" + code;
    }

    @Override
    public void acceptInvitation(String inviteCode, String currentUsername){
        Invitation invite = invitationRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("Invalid invite code!"));

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = invite.getGroup();

        boolean alreadyMember = groupMemberRepository
                .findByGroupAndUserUsername(group, currentUsername).isPresent();

        if (!alreadyMember) {
            GroupMember member = GroupMember.builder()
                    .user(user)
                    .group(group)
                    .build();

            groupMemberRepository.save(member);
        }

        invite.setUsedBy(user);
        invitationRepository.save(invite);
    }
}
