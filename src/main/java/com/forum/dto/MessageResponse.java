package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private UUID id;
    private String userId;
    private String body;
    private LocalDateTime createdAt;
    private Integer upvoteCount;
    private Boolean deleted;
    private LocalDateTime updatedAt;
    private UUID subthreadId;
}
