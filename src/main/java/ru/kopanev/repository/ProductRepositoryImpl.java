package ru.kopanev.repository;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.model.Product;
import ru.kopanev.factory.DataSourceFactory;

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
        String sql = "INSERT INTO marketplace.products (name, category, brand, price, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
        String sql = "UPDATE marketplace.products SET name=?, category=?, brand=?, price=?, description=? WHERE id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "DELETE FROM marketplace.products WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "SELECT id, name, category, brand, price, description FROM marketplace.products ORDER BY id";
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) result.add(mapRowToProduct(rs));
        } catch (SQLException e) {
            log.error("Ошибка получения всех продуктов", e);
        }
        return result;
    }

    public List<Product> findByCategory(String category) {
        String sql = "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE category = ? ORDER BY name";
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE brand = ? ORDER BY name";
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE price BETWEEN ? AND ? ORDER BY price";
        List<Product> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
