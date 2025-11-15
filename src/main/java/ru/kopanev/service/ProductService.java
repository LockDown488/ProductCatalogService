package ru.kopanev.service;

import ru.kopanev.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product getProduct(Long id);

    void addProduct(String username, Product product);

    void updateProduct(String username, Long id, Product updatedProduct);

    void removeProduct(String username, Long id);

    List<Product> getAllProducts();

    List<Product> findByCategory(String username, String category);

    List<Product> findByBrand(String username, String brand);

    List<Product> findByPriceRange(String username, BigDecimal minPrice, BigDecimal maxPrice);
}