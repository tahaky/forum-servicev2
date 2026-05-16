package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CommentResponse {

    private UUID id;
    private UUID topicId;
    private UUID userId;
    private String comment;
}
