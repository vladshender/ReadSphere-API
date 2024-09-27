package com.example.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateBookRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull
    @DecimalMin(value = "10")
    private BigDecimal price;

    @NotBlank
    private String description;

    @NotBlank
    private String coverImage;

    @NotEmpty
    private List<Long> categories;
}
