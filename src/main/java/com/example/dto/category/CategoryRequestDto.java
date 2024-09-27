package com.example.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank
    @Size(max = 30)
    private String name;

    @Size(max = 100)
    private String description;
}
