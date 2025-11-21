package ru.kopanev.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.model.Product;
import ru.kopanev.factory.DataSourceFactory;
import ru.kopanev.repository.ProductRepository;
import ru.kopanev.utils.SqlQueries;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Slf4j
public class ProductRepositoryImpl implements ProductRepository {
    private final DataSource dataSource;

    public ProductRepositoryImpl() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    public void save(Product product) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.SAVE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setString(3, product.getBrand());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setString(5, product.getDescription());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getLong(1));
                }
            }
            log.info("Product сохранён, id={}", product.getId());
        } catch (SQLException e) {
            log.error("Ошибка сохранения продукта: {}", product, e);
            throw new RuntimeException("Failed to save product", e);
        }
    }

    public void update(Product product) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.UPDATE_PRODUCT)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setString(3, product.getBrand());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setString(5, product.getDescription());
            stmt.setLong(6, product.getId());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new RuntimeException("Product not found: " + product.getId());
            log.info("Product обновлён, id={}", product.getId());
        } catch (SQLException e) {
            log.error("Ошибка обновления продукта: {}", product, e);
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public void delete(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.DELETE_PRODUCT)) {

            stmt.setLong(1, id);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new RuntimeException("Product not found: " + id);
            log.info("Product удалён, id={}", id);
        } catch (SQLException e) {
            log.error("Ошибка удаления продукта, id={}", id, e);
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    public Optional<Product> findById(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_PRODUCT_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка поиска продукта, id={}", id, e);
        }
        return Optional.empty();
    }

    public List<Product> findAll() {
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_ALL_PRODUCTS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) result.add(mapRowToProduct(rs));
        } catch (SQLException e) {
            log.error("Ошибка получения всех продуктов", e);
        }
        return result;
    }

    public List<Product> findByCategory(String category) {
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_PRODUCT_BY_CATEGORY)) {

            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToProduct(rs));
                }
            }
            log.debug("Found {} products in category '{}'", result.size(), category);
        } catch (SQLException e) {
            log.error("Failed to find products by category: {}", category, e);
        }
        return result;
    }

    public List<Product> findByBrand(String brand) {
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_PRODUCT_BY_BRAND)) {

            stmt.setString(1, brand);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToProduct(rs));
                }
            }
            log.debug("Found {} products of brand '{}'", result.size(), brand);
        } catch (SQLException e) {
            log.error("Failed to find products by brand: {}", brand, e);
        }
        return result;
    }

    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_PRODUCT_BY_PRICE_RANGE)) {

            stmt.setBigDecimal(1, minPrice);
            stmt.setBigDecimal(2, maxPrice);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToProduct(rs));
                }
            }
            log.debug("Found {} products in price range [{}, {}]", result.size(), minPrice, maxPrice);
        } catch (SQLException e) {
            log.error("Failed to find products by price range: [{}, {}]", minPrice, maxPrice, e);
        }
        return result;
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("brand"),
                rs.getBigDecimal("price"),
                rs.getString("description")
        );
    }
}
