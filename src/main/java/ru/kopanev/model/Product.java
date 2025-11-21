package ru.kopanev.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Представляет товар в системе маркетплейса.
 * Содержит информацию о названии, категории, бренде, цене и описании товара.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;
    private String name;
    private String category;
    private String brand;
    private BigDecimal price;
    private String description;

    public Product(String name, String category, String brand, BigDecimal price, String description) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.description = description;
    }
}
