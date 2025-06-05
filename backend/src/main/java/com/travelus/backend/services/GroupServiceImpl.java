package com.travelus.backend.services;

import com.travelus.backend.dtos.GroupRequest;
import com.travelus.backend.dtos.GroupResponse;
import com.travelus.backend.models.Group;
import com.travelus.backend.models.GroupMember;
import com.travelus.backend.models.User;
import com.travelus.backend.repositories.GroupMemberRepository;
import com.travelus.backend.repositories.GroupRepository;
import com.travelus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService{
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Override
    public Long createGroup (GroupRequest request, String creatorUsername){
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = new Group();
        group.setName(request.getGroupName());
        group.setAdmin(creator);
        Group savedGroup = groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(savedGroup);
        groupMember.setUser(creator);
        groupMemberRepository.save(groupMember);

        return savedGroup.getId();
    }

    @Override
    public List<GroupResponse> getGroupsForUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GroupMember> memberships = groupMemberRepository.findByUser(user);

        return memberships.stream().map(membership -> {
            Group group = membership.getGroup();
            List<String> members = groupMemberRepository.findByGroup(group)
                    .stream()
                    .map(m -> m.getUser().getUsername())
                    .collect(Collectors.toList());

            GroupResponse response = new GroupResponse();
            response.setGroupId(group.getId());
            response.setGroupName(group.getName());
            response.setAdminUsername(group.getAdmin().getUsername());
            response.setMembers(members);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public GroupResponse getGroupDetails(Long groupId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean isMember = groupMemberRepository
                .findByGroupAndUserUsername(group, requesterUsername)
                .isPresent();

        if (!isMember) {
            throw new RuntimeException("Unauthorized: Not a group member");
        }

        List<String> members = groupMemberRepository.findByGroup(group)
                .stream()
                .map(m -> m.getUser().getUsername())
                .collect(Collectors.toList());

        GroupResponse response = new GroupResponse();
        response.setGroupId(group.getId());
        response.setGroupName(group.getName());
        response.setAdminUsername(group.getAdmin().getUsername());
        response.setMembers(members);

        return response;
    }
}
