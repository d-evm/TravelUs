package com.travelus.backend.controllers;

import com.travelus.backend.dtos.GroupRequest;
import com.travelus.backend.dtos.GroupResponse;
import com.travelus.backend.services.GroupService;
import com.travelus.backend.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails){
        Long groupId = groupService.createGroup(request, userDetails.getUsername());
        return ResponseEntity.ok("Group created with ID: " + groupId);
    }

    @GetMapping("/my")
    public ResponseEntity<List<GroupResponse>> getMyGroups (@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(groupService.getGroupsForUser(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupDetails (@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(groupService.getGroupDetails(id, userDetails.getUsername()));
    }

    @PostMapping("/{groupId}/invite")
    public ResponseEntity<?> inviteToGroup (@PathVariable Long groupId,
                                            @AuthenticationPrincipal UserDetails userDetails){
        String link = invitationService.createInvitation(groupId, userDetails.getUsername());

        return ResponseEntity.ok("Invite link: " + link);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGroup (@RequestParam String code,
                                        @AuthenticationPrincipal UserDetails userDetails){
        invitationService.acceptInvitation(code, userDetails.getUsername());
        return ResponseEntity.ok("Successfully joined the group!");
    }

}
