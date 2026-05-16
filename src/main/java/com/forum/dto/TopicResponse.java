package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TopicResponse {

    private UUID id;
    private UUID userId;
    private String topicName;
    private String initialMessage;
}
