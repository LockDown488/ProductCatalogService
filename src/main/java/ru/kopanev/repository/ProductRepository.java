package ru.kopanev.repository;

import ru.kopanev.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();

    public void save(Product product) {
        products.put(product.getId(), product);
    }

    public void replace(Long id, Product product) {
        products.replace(id, product);
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public void remove(Long id) {
        products.remove(id);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public List<Product> findByCategory(String category) {
        return products.values().stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Product> findByBrand(String brand) {
        return products.values().stream()
                .filter(product -> product.getBrand().equalsIgnoreCase(brand))
                .toList();
    }

    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return products.values().stream()
                .filter(product -> product.getPrice().doubleValue() >= minPrice.doubleValue() && product.getPrice().doubleValue() <= maxPrice.doubleValue())
                .toList();
    }
}
