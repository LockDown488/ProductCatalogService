package ru.kopanev.ui;

import lombok.RequiredArgsConstructor;
import ru.kopanev.exception.EntityNotFoundException;
import ru.kopanev.model.Product;
import ru.kopanev.service.ProductService;
import ru.kopanev.utils.UserSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class ProductUi {
    private final Scanner scanner;
    private final UserSession session;

    private final ProductService productService;

    public void addProduct() {
        System.out.print("Введите название товара: ");
        String name = scanner.nextLine().trim();
        System.out.print("Введите категорию: ");
        String category = scanner.nextLine().trim();
        System.out.print("Введите бренд: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Введите цену: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.println("Введите описание: ");
        String description = scanner.nextLine().trim();

        Product product = new Product(name, category, brand, BigDecimal.valueOf(price), description);
        productService.addProduct(session.getCurrentUser(), product);
        System.out.println("Товар добавлен с ID: " + product.getId());
    }

    public void updateProduct() {
        System.out.print("Введите ID товара для изменения: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        if (!getProductsIds().contains(id)) {
            System.out.println("Товара с ID " + id + " нет в каталоге");
            return;
        }

        try {
            System.out.print("Введите новое название: ");
            String name = scanner.nextLine().trim();
            System.out.print("Введите новую категорию: ");
            String category = scanner.nextLine().trim();
            System.out.print("Введите новый бренд: ");
            String brand = scanner.nextLine().trim();
            System.out.print("Введите новую цену: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Введите новое описание: ");
            String description = scanner.nextLine().trim();

            Product updatedProduct = new Product(name, category, brand, BigDecimal.valueOf(price), description);
            productService.updateProduct(session.getCurrentUser(), id, updatedProduct);
            System.out.println("Товар обновлен");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProduct() {
        System.out.print("Введите ID товара для удаления: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        if (!getProductsIds().contains(id)) {
            System.out.println("Товара с ID " + id + " нет в каталоге");
            return;
        }

        try {
            productService.removeProduct(session.getCurrentUser(), id);
            System.out.println("Товар удален");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Каталог пуст");
            return;
        }

        System.out.println("\n=== КАТАЛОГ ТОВАРОВ ===");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public void filterByCategory() {
        System.out.println("Введите категорию: ");
        String category = scanner.nextLine().trim();

        if (!getProductsCategories().contains(category)) {
            System.out.println("Товаров с такой категорией нет в каталоге");
            return;
        }

        List<Product> filteredProducts = productService.findByCategory(session.getCurrentUser(), category);
        System.out.println("\n=== ТОВАРЫ ПО КАТЕГОРИИ " + category + " ===");
        for (Product product : filteredProducts) {
            System.out.println(product);
        }
    }

    public void filterByBrand() {
        System.out.println("Введите бренд: ");
        String brand = scanner.nextLine().trim();

        if (!getProductsBrands().contains(brand)) {
            System.out.println("Товаров от этого бренда нет в каталоге");
            return;
        }

        List<Product> filteredProducts = productService.findByBrand(session.getCurrentUser(), brand);
        System.out.println("\n=== ТОВАРЫ ПО БРЕНДУ " + brand + " ===");
        for (Product product : filteredProducts) {
            System.out.println(product);
        }
    }

    public void filterByPriceRange() {
        System.out.println("Введите минимальную цену товаров: ");
        BigDecimal minPrice = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine().trim()));
        System.out.println("Введите максимальную цену товаров: ");
        BigDecimal maxPrice = BigDecimal.valueOf(Double.parseDouble(scanner.nextLine().trim()));

        List<Product> filteredProducts = productService.findByPriceRange(session.getCurrentUser(), minPrice, maxPrice);
        System.out.println("\n=== ТОВАРЫ ОТ " + minPrice + " ДО " + maxPrice + " ===");
        for (Product product : filteredProducts) {
            System.out.println(product);
        }
    }

    public void getProductById() {
        System.out.print("Введите ID товара: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        try {
            Product product = productService.getProduct(id);
            System.out.println("\n=== ТОВАР ===");
            System.out.println(product);
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Long> getProductsIds() {
        return productService.getAllProducts().stream()
                .map(Product::getId)
                .toList();
    }

    private List<String> getProductsCategories() {
        return productService.getAllProducts().stream()
                .map(Product::getCategory)
                .toList();
    }

    private List<String> getProductsBrands() {
        return productService.getAllProducts().stream()
                .map(Product::getBrand)
                .toList();
    }
}
