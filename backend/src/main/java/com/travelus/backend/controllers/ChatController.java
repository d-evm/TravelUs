package com.travelus.backend.controllers;

import com.travelus.backend.dtos.ChatMessageDTO;
import com.travelus.backend.models.Group;
import com.travelus.backend.models.Message;
import com.travelus.backend.models.User;
import com.travelus.backend.repositories.GroupRepository;
import com.travelus.backend.repositories.MessageRepository;
import com.travelus.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat/{groupId}")
    public void sendMessage (@DestinationVariable Long groupId,
                             @Payload ChatMessageDTO chatMessageDTO,
//                             @Header("simpUser") Principal principal){
                             Principal principal){

        if (principal == null) {
            System.out.println("Principal is null! WebSocket not authenticated.");
            return;
        }

        String username = principal.getName();
        System.out.println("WebSocket Principal: " + username);

        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Group group = groupRepository.findById(groupId).orElseThrow();

        System.out.println("Received WebSocket message from user: " + sender.getName());

        Message message = Message.builder()
                .content(chatMessageDTO.getMessageText())
                .group(group)
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        ChatMessageDTO outgoingMessage = ChatMessageDTO.builder()
                .messageText(chatMessageDTO.getMessageText())
                .senderName(sender.getName())
                .groupId(groupId)
                .timestamp(message.getTimestamp())
                .build();

        messagingTemplate.convertAndSend("/topic/groups/" + groupId, outgoingMessage);

    }
}
