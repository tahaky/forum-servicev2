package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubthreadResponse {
    private UUID id;
    private String userId;
    private String title;
    private LocalDateTime createdAt;
    private UUID threadId;
    private List<MessageResponse> messages;
}
