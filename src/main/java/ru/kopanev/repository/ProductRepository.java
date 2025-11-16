package ru.kopanev.repository;

import ru.kopanev.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductRepository {

    void save(Product product);

    void update(Product product);

    void delete(Long id);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
}
