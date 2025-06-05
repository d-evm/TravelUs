package com.travelus.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GroupResponse {
    private Long groupId;
    private String groupName;
    private String adminUsername;
    private List<String> members;
}
