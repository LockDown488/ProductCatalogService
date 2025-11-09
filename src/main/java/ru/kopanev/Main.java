package ru.kopanev;

import ru.kopanev.enums.Action;
import ru.kopanev.exception.EntityNotFoundException;
import ru.kopanev.model.Event;
import ru.kopanev.model.Product;
import ru.kopanev.repository.ProductRepository;
import ru.kopanev.service.AuditService;
import ru.kopanev.service.AuthService;
import ru.kopanev.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Random random = new Random();

    private static final AuditService auditService = new AuditService();
    private static final AuthService authService = new AuthService();
    private static final ProductRepository productRepository = new ProductRepository();
    private static final ProductService productService = new ProductService(productRepository, auditService);

    private static String currentUser = null;

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                showGuestMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private static void showGuestMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");

        String input = sc.nextLine().trim();
        switch (input) {
            case "1": register(); break;
            case "2": login(); break;
            case "0": System.out.println("Выход. До свидания!"); System.exit(0); break;
            default: System.out.println("Неверный ввод, попробуйте еще раз."); break;
        }
    }

    private static void showUserMenu() {
        System.out.println("\n=== ПОЛЬЗОВАТЕЛЬСКОЕ МЕНЮ ===");
        System.out.println("1. Добавить товар");
        System.out.println("2. Изменить товар");
        System.out.println("3. Удалить товар");
        System.out.println("4. Просмотреть все товары");
        System.out.println("5. Найти товары по категории");
        System.out.println("6. Найти товары по бренду");
        System.out.println("7. Найти товары в ценовом диапазоне");
        System.out.println("8. Получить товар по ID");
        System.out.println("9. Просмотреть аудит");
        System.out.println("0. Выйти");
        System.out.print("Выберите действие: ");

        String input = sc.nextLine().trim();
        switch (input) {
            case "1": addProduct(); break;
            case "2": updateProduct(); break;
            case "3": deleteProduct(); break;
            case "4": listProducts(); break;
            case "5": filterByCategory(); break;
            case "6": filterByBrand(); break;
            case "7": filterByPriceRange(); break;
            case "8": getProductById(); break;
            case "9": viewAudit(); break;
            case "0": logout(); break;
            default: System.out.println("Неверный ввод, попробуйте еще раз."); break;
        }
    }

    private static void register() {
        System.out.print("Введите имя пользователя: ");
        String username = sc.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = sc.nextLine().trim();

        if (authService.register(username, password)) {
            System.out.println("Регистрация успешна");
        } else {
            System.out.println("Пользователь с таким именем уже существует");
        }
    }

    private static void login() {
        System.out.print("Введите имя пользователя: ");
        String username = sc.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = sc.nextLine().trim();

        if (authService.login(username, password)) {
            currentUser = username;
            System.out.println("Вход выполнен");
        } else {
            System.out.println("Неверное имя пользователя или пароль");
        }
    }

    private static void logout() {
        System.out.println("Пользователь " + currentUser + " вышел");
        auditService.log(currentUser, Action.LOGOUT, "Пользователь вышел");
        currentUser = null;
    }

    private static void addProduct() {
        System.out.print("Введите название товара: ");
        String name = sc.nextLine().trim();
        System.out.print("Введите категорию: ");
        String category = sc.nextLine().trim();
        System.out.print("Введите бренд: ");
        String brand = sc.nextLine().trim();
        System.out.print("Введите цену: ");
        double price = Double.parseDouble(sc.nextLine().trim());
        System.out.println("Введите описание: ");
        String description = sc.nextLine().trim();

        Product product = new Product(generateUniqueId(), name, category, brand, BigDecimal.valueOf(price), description);
        productService.addProduct(currentUser, product);
        System.out.println("Товар добавлен с ID: " + product.getId());
    }

    private static void updateProduct() {
        System.out.print("Введите ID товара для изменения: ");
        long id = Long.parseLong(sc.nextLine().trim());

        if (!getProductsIds().contains(id)) {
            System.out.println("Товара с ID " + id + " нет в каталоге");
            return;
        }

        try {
            System.out.print("Введите новое название: ");
            String name = sc.nextLine().trim();
            System.out.print("Введите новую категорию: ");
            String category = sc.nextLine().trim();
            System.out.print("Введите новый бренд: ");
            String brand = sc.nextLine().trim();
            System.out.print("Введите новую цену: ");
            double price = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Введите новое описание: ");
            String description = sc.nextLine().trim();

            Product updatedProduct = new Product(id, name, category, brand, BigDecimal.valueOf(price), description);
            productService.updateProduct(currentUser, id, updatedProduct);
            System.out.println("Товар обновлен");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteProduct() {
        System.out.print("Введите ID товара для удаления: ");
        long id = Long.parseLong(sc.nextLine().trim());

        if (!getProductsIds().contains(id)) {
            System.out.println("Товара с ID " + id + " нет в каталоге");
            return;
        }

        try {
            productService.removeProduct(currentUser, id);
            System.out.println("Товар удален");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listProducts() {
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

    private static void filterByCategory() {
        System.out.println("Введите категорию: ");
        String category = sc.nextLine().trim();

        if (!getProductsCategories().contains(category)) {
            System.out.println("Товаров с такой категорией нет в каталоге");
            return;
        }

        List<Product> filteredProducts = productService.findByCategory(currentUser, category);
        System.out.println("\n=== ТОВАРЫ ПО КАТЕГОРИИ " + category + " ===");
        for (Product product : filteredProducts) {
            System.out.println(product);
        }
    }

    private static void filterByBrand() {
        System.out.println("Введите бренд: ");
        String brand = sc.nextLine().trim();

        if (!getProductsBrands().contains(brand)) {
            System.out.println("Товаров от этого бренда нет в каталоге");
            return;
        }

        List<Product> filteredProducts = productService.findByBrand(currentUser, brand);
        System.out.println("\n=== ТОВАРЫ ПО БРЕНДУ " + brand + " ===");
        for (Product product : filteredProducts) {
            System.out.println(product);
        }
    }

    private static void filterByPriceRange() {
        System.out.println("Введите минимальную цену товаров: ");
        BigDecimal minPrice = BigDecimal.valueOf(Double.parseDouble(sc.nextLine().trim()));
        System.out.println("Введите максимальную цену товаров: ");
        BigDecimal maxPrice = BigDecimal.valueOf(Double.parseDouble(sc.nextLine().trim()));

        List<Product> filteredProducts = productService.findByPriceRange(currentUser, minPrice, maxPrice);
        System.out.println("\n=== ТОВАРЫ ОТ " + minPrice + " ДО " + maxPrice + " ===");
        for (Product product : filteredProducts) {
            System.out.println(product);
        }
    }

    private static void getProductById() {
        System.out.print("Введите ID товара: ");
        long id = Long.parseLong(sc.nextLine().trim());

        try {
            Product product = productService.getProduct(id);
            System.out.println("\n=== ТОВАР ===");
            System.out.println(product);
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void viewAudit() {
        List<Event> events = auditService.getEvents();

        if (events.isEmpty()) {
            System.out.println("Журнал аудита пуст.");
            return;
        }

        System.out.println("\n=== ЖУРНАЛ АУДИТА ===");
        for (Event event : events) {
            System.out.println(event);
        }
    }

    private static Long generateUniqueId() {
        Long id = random.nextLong(10000);

        while (getProductsIds().contains(id)) {
            id = random.nextLong(10000);
        }

        return id;
    }

    private static List<Long> getProductsIds() {
        return productService.getAllProducts().stream()
                .map(Product::getId)
                .toList();
    }

    private static List<String> getProductsCategories() {
        return productService.getAllProducts().stream()
                .map(Product::getCategory)
                .toList();
    }

    private static List<String> getProductsBrands() {
        return productService.getAllProducts().stream()
                .map(Product::getBrand)
                .toList();
    }
}