package com.forum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAttributeRequest {

    @NotBlank
    private String attr;

    @NotBlank
    private String value;
}
