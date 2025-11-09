package ru.kopanev.utils;

import ru.kopanev.model.Product;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProductCache {

    private final Map<Long, Product> cache;
    private final int maxSize;

    public ProductCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<Long, Product>(maxSize, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<Long, Product> eldest) {
                return size() > ProductCache.this.maxSize;
            }
        };
    }

    public Product get(Long id) {
        return cache.get(id);
    }

    public void put(Product product) {
        cache.put(product.getId(), product);
    }

    public void clear() {
        cache.clear();
    }
}
