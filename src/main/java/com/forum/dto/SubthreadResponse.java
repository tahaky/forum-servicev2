package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
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
}
