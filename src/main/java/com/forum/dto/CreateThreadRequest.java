package com.forum.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateThreadRequest {
    private String userId;
    private String type;
    private UUID vehicleBrandId;
    private String title;
}
