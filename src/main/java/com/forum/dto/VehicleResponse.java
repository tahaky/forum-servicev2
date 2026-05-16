package com.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VehicleResponse {

    private UUID id;
    private String brand;
}
