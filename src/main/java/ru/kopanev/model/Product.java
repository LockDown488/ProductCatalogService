package ru.kopanev.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    private String name;
    private String category;
    private String brand;
    private BigDecimal price;
    private String description;
}
