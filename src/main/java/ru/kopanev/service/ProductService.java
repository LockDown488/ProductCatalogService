package ru.kopanev.service;

import ru.kopanev.enums.Action;
import ru.kopanev.exception.EntityNotFoundException;
import ru.kopanev.model.Product;
import ru.kopanev.repository.ProductRepository;
import ru.kopanev.utils.ProductCache;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;
    private final AuditService auditService;
    private final ProductCache productCache = new ProductCache(50);

    public ProductService(ProductRepository productRepository, AuditService auditService) {
        this.productRepository = productRepository;
        this.auditService = auditService;
    }

    public Product getProduct(Long id) {
        Product cached = productCache.get(id);
        if (cached != null) {
            return cached;
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Товар с ID " + id + " не найден"));
        productCache.put(product);

        return product;
    }

    public void addProduct(String username, Product product) {
        productRepository.save(product);
        productCache.put(product);
        auditService.log(username, Action.ADD_PRODUCT, "Добавлен товар: " + product.getName());
    }

    public void updateProduct(String username, Long id, Product updatedProduct) {
        productRepository.replace(id, updatedProduct);
        productCache.put(updatedProduct);
        auditService.log(username, Action.UPDATE_PRODUCT, "Обновлен товар ID: " + id);
    }

    public void removeProduct(String username, Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Товар с ID " + id + " не найден"));

        productRepository.remove(id);
        productCache.clear();
        auditService.log(username, Action.REMOVE_PRODUCT, "Удален товар ID: " + id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(String username, String category) {
        auditService.log(username, Action.FILTERED_BY_CATEGORY, "Отсортированы предметы по категории " + category);
        return productRepository.findByCategory(category);
    }

    public List<Product> findByBrand(String username, String brand) {
        auditService.log(username, Action.FILTERED_BY_BRAND, "Отсортированы предметы по бренду " + brand);
        return productRepository.findByBrand(brand);
    }

    public List<Product> findByPriceRange(String username, BigDecimal minPrice, BigDecimal maxPrice) {
        auditService.log(username, Action.FILTERED_BY_PRICE_RANGE, "Отсортированы предметы по цене от " + minPrice + " до " + maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
}