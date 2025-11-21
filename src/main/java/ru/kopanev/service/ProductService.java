package ru.kopanev.service;

import ru.kopanev.exception.EntityNotFoundException;
import ru.kopanev.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для управления товарами.
 * Обрабатывает бизнес-логику работы с товарами, включая CRUD операции,
 * поиск и фильтрацию. Автоматически логирует все действия пользователей.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public interface ProductService {

    /**
     * Возвращает товар по ID.
     *
     * @param id идентификатор товара
     * @return найденный товар
     * @throws EntityNotFoundException если товар не найден
     */
    Product getProduct(Long id);

    /**
     * Добавляет новый товар в систему.
     * Логирует действие в аудит.
     *
     * @param username имя пользователя, добавляющего товар
     * @param product товар для добавления
     */
    void addProduct(String username, Product product);

    /**
     * Обновляет существующий товар.
     * Логирует действие в аудит.
     *
     * @param username имя пользователя, обновляющего товар
     * @param product товар с обновлёнными данными
     * @throws EntityNotFoundException если товар не найден
     */
    void updateProduct(String username, Product product);

    /**
     * Удаляет товар по ID.
     * Логирует действие в аудит.
     *
     * @param username имя пользователя, удаляющего товар
     * @param id идентификатор товара для удаления
     * @throws EntityNotFoundException если товар не найден
     */
    void deleteProduct(String username, Long id);

    /**
     * Возвращает все товары из системы.
     *
     * @return список всех товаров
     */
    List<Product> getAllProducts();

    /**
     * Находит товары по категории.
     * Логирует действие в аудит.
     *
     * @param username имя пользователя, выполняющего поиск
     * @param category название категории
     * @return список товаров указанной категории
     */
    List<Product> findByCategory(String username, String category);

    /**
     * Находит товары по бренду.
     * Логирует действие в аудит.
     *
     * @param username имя пользователя, выполняющего поиск
     * @param brand название бренда
     * @return список товаров указанного бренда
     */
    List<Product> findByBrand(String username, String brand);

    /**
     * Находит товары в диапазоне цен.
     * Логирует действие в аудит.
     *
     * @param username имя пользователя, выполняющего поиск
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @return список товаров в указанном ценовом диапазоне
     */
    List<Product> findByPriceRange(String username, BigDecimal minPrice, BigDecimal maxPrice);
}