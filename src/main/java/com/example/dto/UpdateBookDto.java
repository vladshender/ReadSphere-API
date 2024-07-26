package com.example.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateBookDto {
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
}
