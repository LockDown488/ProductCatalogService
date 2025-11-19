package ru.kopanev.repository;

import ru.kopanev.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий для работы с товарами.
 * Предоставляет методы для выполнения CRUD операций и поиска товаров
 * в базе данных по различным критериям.
 *
 * <p>Все операции выполняются в схеме {@code marketplace} базы данных PostgreSQL.</p>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public interface ProductRepository {

    /**
     * Сохраняет новый товар в базу данных.
     * После успешного сохранения товару присваивается уникальный идентификатор,
     * который устанавливается в поле {@code id} объекта.
     *
     * @param product товар для сохранения (не должен быть {@code null})
     * @throws IllegalArgumentException если product равен {@code null}
     * @throws RuntimeException если произошла ошибка при сохранении в БД
     */
    void save(Product product);

    /**
     * Обновляет существующий товар в базе данных.
     * Обновляются все поля товара на основе его идентификатора.
     *
     * @param product товар с обновлёнными данными (не должен быть {@code null},
     *                должен иметь существующий {@code id})
     * @throws IllegalArgumentException если product равен {@code null} или id не установлен
     * @throws RuntimeException если товар с указанным id не найден или произошла ошибка БД
     */
    void update(Product product);

    /**
     * Удаляет товар из базы данных по его идентификатору.
     *
     * @param id уникальный идентификатор товара (должен быть положительным числом)
     * @throws IllegalArgumentException если id равен {@code null} или меньше 1
     * @throws RuntimeException если произошла ошибка при удалении из БД
     */
    void delete(Long id);

    /**
     * Находит товар по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор товара (должен быть положительным числом)
     * @return {@link Optional} содержащий найденный товар, или пустой {@code Optional},
     *         если товар с указанным id не найден
     * @throws IllegalArgumentException если id равен {@code null} или меньше 1
     * @throws RuntimeException если произошла ошибка при обращении к БД
     */
    Optional<Product> findById(Long id);

    /**
     * Возвращает все товары из базы данных.
     * Товары не отсортированы в определённом порядке.
     *
     * @return список всех товаров; может быть пустым, но никогда не {@code null}
     * @throws RuntimeException если произошла ошибка при обращении к БД
     */
    List<Product> findAll();

    /**
     * Находит все товары в указанной категории.
     * Поиск выполняется с учётом регистра символов.
     *
     * @param category название категории для поиска (не должно быть {@code null} или пустым)
     * @return список товаров в указанной категории; может быть пустым, но никогда не {@code null}
     * @throws IllegalArgumentException если category равен {@code null} или пуст
     * @throws RuntimeException если произошла ошибка при обращении к БД
     */
    List<Product> findByCategory(String category);

    /**
     * Находит все товары указанного бренда.
     * Поиск выполняется с учётом регистра символов.
     *
     * @param brand название бренда для поиска (не должно быть {@code null} или пустым)
     * @return список товаров указанного бренда; может быть пустым, но никогда не {@code null}
     * @throws IllegalArgumentException если brand равен {@code null} или пуст
     * @throws RuntimeException если произошла ошибка при обращении к БД
     */
    List<Product> findByBrand(String brand);

    /**
     * Находит все товары в указанном диапазоне цен (включительно).
     * Возвращаются товары, цена которых больше или равна {@code minPrice}
     * и меньше или равна {@code maxPrice}.
     *
     * @param minPrice минимальная цена (должна быть неотрицательной и не {@code null})
     * @param maxPrice максимальная цена (должна быть больше или равна minPrice и не {@code null})
     * @return список товаров в указанном ценовом диапазоне; может быть пустым, но никогда не {@code null}
     * @throws IllegalArgumentException если minPrice или maxPrice равны {@code null},
     *                                  minPrice отрицательна, или minPrice больше maxPrice
     * @throws RuntimeException если произошла ошибка при обращении к БД
     */
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
}
