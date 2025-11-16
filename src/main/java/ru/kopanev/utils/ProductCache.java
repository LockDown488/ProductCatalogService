package ru.kopanev.utils;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.model.Product;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ProductCache {

    private final Map<Long, Product> cache;
    private final int maxSize;

    public ProductCache() {
        this(100);
    }

    public ProductCache(int maxSize) {
        this.cache = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        log.info("ProductCache initialized with max size: {}", maxSize);
    }

    /**
     * Получает товар из кэша.
     */
    public Optional<Product> get(Long id) {
        Product product = cache.get(id);
        if (product != null) {
            log.debug("Cache HIT: product id={}", id);
        } else {
            log.debug("Cache MISS: product id={}", id);
        }
        return Optional.ofNullable(product);
    }

    /**
     * Добавляет товар в кэш.
     */
    public void put(Product product) {
        if (cache.size() >= maxSize) {
            evictOldest();
        }
        cache.put(product.getId(), product);
        log.debug("Cached product: id={}, name={}", product.getId(), product.getName());
    }

    /**
     * Обновляет товар в кэше.
     */
    public void update(Product product) {
        cache.put(product.getId(), product);
        log.debug("Updated cache: product id={}", product.getId());
    }

    /**
     * Удаляет товар из кэша.
     */
    public void invalidate(Long id) {
        cache.remove(id);
        log.debug("Invalidated cache: product id={}", id);
    }

    /**
     * Очищает весь кэш.
     */
    public void clear() {
        cache.clear();
        log.info("Cache cleared");
    }

    /**
     * Проверяет, есть ли товар в кэше.
     */
    public boolean contains(Long id) {
        return cache.containsKey(id);
    }

    /**
     * Возвращает размер кэша.
     */
    public int size() {
        return cache.size();
    }

    /**
     * Вытесняет самый старый элемент (простая стратегия).
     * В production лучше использовать LRU (Least Recently Used).
     */
    private void evictOldest() {
        if (!cache.isEmpty()) {
            Long firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
            log.debug("Evicted product from cache: id={}", firstKey);
        }
    }
}
