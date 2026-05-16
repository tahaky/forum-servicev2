package com.forum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateVehicleRequest {

    @NotBlank
    private String brand;
}
