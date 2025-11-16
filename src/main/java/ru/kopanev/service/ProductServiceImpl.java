package ru.kopanev.service;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.enums.Action;
import ru.kopanev.exception.EntityNotFoundException;
import ru.kopanev.model.Product;
import ru.kopanev.repository.ProductRepository;
import ru.kopanev.utils.ProductCache;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AuditService auditService;
    private final ProductCache productCache;

    public ProductServiceImpl(ProductRepository productRepository, AuditService auditService, ProductCache productCache) {
        this.productRepository = productRepository;
        this.auditService = auditService;
        this.productCache = productCache;
    }

    public Product getProduct(Long id) {
        Optional<Product> cachedProduct = productCache.get(id);
        if (cachedProduct.isPresent()) {
            return cachedProduct.get();
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Товар с ID " + id + " не найден"));
        productCache.put(product);

        return product;
    }

    public void addProduct(String username, Product product) {
        log.info("Adding product: {}", product.getName());

        productRepository.save(product);
        productCache.put(product);

        auditService.logAction(username, Action.ADD_PRODUCT, "Добавлен товар: " + product.getName());
    }

    public void updateProduct(String username, Product product) {
        log.info("Updating product: id={}", product.getId());

        productRepository.findById(product.getId())
                .orElseThrow(() -> new EntityNotFoundException("Товар с ID " + product.getId() + " не найден"));

        productRepository.update(product);
        productCache.update(product);

        auditService.logAction(username, Action.UPDATE_PRODUCT, "Обновлен товар: " + product.getName());
    }

    public void deleteProduct(String username, Long id) {
        log.info("Deleting product: id={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Товар с ID " + id + " не найден"));

        productRepository.delete(id);
        productCache.invalidate(id);

        auditService.logAction(username, Action.REMOVE_PRODUCT, "Удален товар: " + product.getName());
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(String username, String category) {
        log.info("Filtering products by category: {}", category);
        auditService.logAction(username, Action.FILTERED_BY_CATEGORY, "Отсортированы предметы по категории " + category);
        return productRepository.findByCategory(category);
    }

    public List<Product> findByBrand(String username, String brand) {
        log.info("Filtering products by brand: {}", brand);
        auditService.logAction(username, Action.FILTERED_BY_BRAND, "Отсортированы предметы по бренду " + brand);
        return productRepository.findByBrand(brand);
    }

    public List<Product> findByPriceRange(String username, BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Filtering products by price range: [{}, {}]", minPrice, maxPrice);
        auditService.logAction(username, Action.FILTERED_BY_PRICE_RANGE, "Отсортированы предметы по цене от " + minPrice + " до " + maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
}
