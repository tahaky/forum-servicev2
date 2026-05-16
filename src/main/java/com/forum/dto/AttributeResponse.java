package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AttributeResponse {

    private UUID id;
    private UUID topicId;
    private String attr;
    private String value;
}
