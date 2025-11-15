package ru.kopanev.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class Product {
    private static final AtomicLong idCounter = new AtomicLong(0);

    private Long id;
    private String name;
    private String category;
    private String brand;
    private BigDecimal price;
    private String description;

    public Product(String name, String category, String brand, BigDecimal price, String description) {
        this.id = idCounter.incrementAndGet();
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.description = description;
    }
}
