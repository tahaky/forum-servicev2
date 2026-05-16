package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VehicleBrandResponse {
    private UUID id;
    private String name;
    private String logoSvg;
    private LocalDateTime createdAt;
}
