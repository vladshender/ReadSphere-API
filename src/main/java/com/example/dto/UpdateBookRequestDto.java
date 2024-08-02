package com.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    @DecimalMin(value = "10")
    private BigDecimal price;

    @NotBlank
    private String description;

    @NotBlank
    private String coverImage;
}
