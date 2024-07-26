package com.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    @Size(min = 13, max = 13)
    private String isbn;

    @NotNull
    @DecimalMin(value = "10")
    private BigDecimal price;

    @NotNull
    private String description;

    @NotNull
    private String coverImage;
}
