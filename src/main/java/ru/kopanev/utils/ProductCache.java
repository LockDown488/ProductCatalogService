package ru.kopanev.utils;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.model.Product;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Кэш для хранения товаров в памяти.
 * Использует ConcurrentHashMap для потокобезопасного доступа.
 *
 * <p>При достижении максимального размера автоматически вытесняет
 * старые элементы (простая FIFO стратегия). Для production рекомендуется
 * использовать LRU (Least Recently Used) стратегию.</p>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Slf4j
public class ProductCache {

    private final Map<Long, Product> cache;
    private final int maxSize;

    /**
     * Создаёт кэш с размером по умолчанию (100 элементов).
     */
    public ProductCache() {
        this(100);
    }

    /**
     * Создаёт кэш с указанным максимальным размером.
     *
     * @param maxSize максимальное количество элементов в кэше
     */
    public ProductCache(int maxSize) {
        this.cache = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        log.info("ProductCache initialized with max size: {}", maxSize);
    }

    /**
     * Получает товар из кэша по идентификатору.
     *
     * @param id идентификатор товара
     * @return Optional с товаром, если найден в кэше, иначе пустой Optional
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
     * Если кэш заполнен, вытесняет самый старый элемент.
     *
     * @param product товар для добавления в кэш
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
     *
     * @param product товар с обновлёнными данными
     */
    public void update(Product product) {
        cache.put(product.getId(), product);
        log.debug("Updated cache: product id={}", product.getId());
    }

    /**
     * Удаляет товар из кэша (инвалидация).
     *
     * @param id идентификатор товара для удаления
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
     * Проверяет наличие товара в кэше.
     *
     * @param id идентификатор товара
     * @return true, если товар находится в кэше
     */
    public boolean contains(Long id) {
        return cache.containsKey(id);
    }

    /**
     * Возвращает текущий размер кэша.
     *
     * @return количество элементов в кэше
     */
    public int size() {
        return cache.size();
    }

    /**
     * Вытесняет самый старый элемент из кэша (FIFO стратегия).
     *
     * <p>Примечание: В production рекомендуется использовать
     * LRU (Least Recently Used) стратегию для более эффективного
     * управления памятью.</p>
     */
    private void evictOldest() {
        if (!cache.isEmpty()) {
            Long firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
            log.debug("Evicted product from cache: id={}", firstKey);
        }
    }
}