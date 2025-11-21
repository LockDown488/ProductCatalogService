package ru.kopanev.ui;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.command.Command;
import ru.kopanev.command.guestCommands.ExitCommand;
import ru.kopanev.command.guestCommands.LoginCommand;
import ru.kopanev.command.guestCommands.RegisterCommand;
import ru.kopanev.command.userCommands.*;
import ru.kopanev.service.*;
import ru.kopanev.utils.UserSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Главное меню приложения маркетплейса.
 * Управляет взаимодействием с пользователем через консольный интерфейс,
 * используя паттерн Command для обработки действий.
 *
 * <p>Отображает два типа меню:</p>
 * <ul>
 *   <li>Гостевое меню (регистрация, вход, выход)</li>
 *   <li>Пользовательское меню (управление товарами, аудит, выход)</li>
 * </ul>
 *
 * <p>Меню автоматически переключается в зависимости от состояния аутентификации
 * пользователя в {@link UserSession}.</p>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Slf4j
public class MenuUi {
    private final Scanner scanner = new Scanner(System.in);
    private final UserSession session;

    private final Map<String, Command> guestCommands = new HashMap<>();
    private final Map<String, Command> userCommands = new HashMap<>();

    /**
     * Создаёт главное меню с инициализацией всех команд.
     * Настраивает карты команд для гостевого и пользовательского меню.
     *
     * @param authService сервис аутентификации
     * @param productService сервис управления товарами
     * @param auditService сервис аудита
     * @param session сессия пользователя
     */
    public MenuUi(AuthService authService, ProductService productService, AuditService auditService, UserSession session) {
        this.session = session;

        ProductUi productUi = new ProductUi(scanner, session, productService);
        UserUi userUi = new UserUi(scanner, session, authService, auditService);
        AuditUi auditUi = new AuditUi(auditService);

        guestCommands.put("1", new RegisterCommand(userUi));
        guestCommands.put("2", new LoginCommand(userUi));
        guestCommands.put("0", new ExitCommand());

        userCommands.put("1", new AddProductCommand(productUi));
        userCommands.put("2", new UpdateProductCommand(productUi));
        userCommands.put("3", new DeleteProductCommand(productUi));
        userCommands.put("4", new ListProductsCommand(productUi));
        userCommands.put("5", new FilterByCategoryCommand(productUi));
        userCommands.put("6", new FilterByBrandCommand(productUi));
        userCommands.put("7", new FilterByPriceRangeCommand(productUi));
        userCommands.put("8", new GetProductCommand(productUi));
        userCommands.put("9", new ViewAllEventsCommand(auditUi));
        userCommands.put("10", new ViewUserEventsCommand(auditUi, session.getCurrentUser()));
        userCommands.put("0", new LogoutCommand(userUi));
    }

    /**
     * Запускает главный цикл приложения.
     * Отображает соответствующее меню в зависимости от состояния аутентификации
     * и обрабатывает команды пользователя до завершения программы.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void start() {
        System.out.println("\n=== ДОБРО ПОЖАЛОВАТЬ В МАРКЕТПЛЕЙС ===\n");

        while (true) {
            if (!session.isAuthenticated()) {
                showGuestMenu();
            } else {
                showUserMenu();
            }
        }
    }

    /**
     * Отображает меню для неавторизованных пользователей.
     * Предоставляет опции регистрации, входа и выхода из приложения.
     */
    private void showGuestMenu() {
        System.out.print("""
                
                === МЕНЮ ===
                1. Регистрация
                2. Вход
                0. Выход
                Выберите действие:\s""");

        String choice = scanner.nextLine().trim();
        Command command = guestCommands.get(choice);
        executeCommand(command);
    }

    /**
     * Отображает меню для авторизованных пользователей.
     * Предоставляет полный набор функций для управления товарами,
     * просмотра аудита и выхода из системы.
     */
    public void showUserMenu() {
        String username = session.getCurrentUser();
        System.out.println("\n=== ПОЛЬЗОВАТЕЛЬСКОЕ МЕНЮ (" + username + ") ===");
        System.out.print("""
        
        1. Добавить товар
        2. Изменить товар
        3. Удалить товар
        4. Просмотреть все товары
        5. Найти товары по категории
        6. Найти товары по бренду
        7. Найти товары в ценовом диапазоне
        8. Получить товар по ID
        9. Просмотреть весь аудит
        10. Просмотреть аудит пользователя
        0. Выйти
        Выберите действие:\s""");

        String choice = scanner.nextLine().trim();
        Command command = userCommands.get(choice);
        executeCommand(command);
    }

    /**
     * Выполняет выбранную команду.
     * Если команда не найдена, выводит сообщение об ошибке.
     *
     * @param command команда для выполнения (может быть null)
     */
    private void executeCommand(Command command) {
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Неверный ввод, попробуйте еще раз.");
        }
    }
}
