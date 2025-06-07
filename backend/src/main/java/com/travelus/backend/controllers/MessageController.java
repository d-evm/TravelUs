package com.travelus.backend.controllers;

import com.travelus.backend.dtos.ChatMessageDTO;
import com.travelus.backend.models.Group;
import com.travelus.backend.models.Message;
import com.travelus.backend.repositories.GroupRepository;
import com.travelus.backend.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups/{groupId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;

    @GetMapping
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Group group = groupRepository.findById(groupId).orElseThrow();

        List<Message> messages = messageRepository.findByGroupOrderByTimestampAsc(group);

        List<ChatMessageDTO> chatHistory = messages.stream().map(msg ->
                ChatMessageDTO.builder()
                        .senderName(msg.getSender().getName())
                        .groupId(groupId)
                        .messageText(msg.getContent())
                        .timestamp(msg.getTimestamp())
                        .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(chatHistory);
    }

}
