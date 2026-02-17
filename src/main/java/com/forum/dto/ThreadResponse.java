package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadResponse {
    private UUID id;
    private String userId;
    private String type;
    private String modelId;
    private String title;
    private LocalDateTime createdAt;
}
