package com.travelus.backend.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {

    private String senderName;
    private Long groupId;
    private String messageText;
    private LocalDateTime timestamp;
}
