package com.travelus.backend.services;

import com.travelus.backend.dtos.GroupRequest;
import com.travelus.backend.dtos.GroupResponse;

import java.util.List;

public interface GroupService {
    Long createGroup(GroupRequest request, String creatorUsername);
    List<GroupResponse> getGroupsForUser(String username);
    GroupResponse getGroupDetails (Long groupId, String requesterUsername);
}
