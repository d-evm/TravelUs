package com.travelus.backend.repositories;


import com.travelus.backend.models.Group;
import com.travelus.backend.models.GroupMember;
import com.travelus.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByGroupId(Long groupId);
    List<GroupMember> findByUserId(Long userId);

    List<GroupMember> findByUser(User user);

    List<GroupMember> findByGroup(Group group);

    Optional<GroupMember> findByGroupAndUserUsername(Group group, String username);
}