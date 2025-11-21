package ru.kopanev.factory;

import ru.kopanev.repository.*;
import ru.kopanev.repository.impl.EventRepositoryImpl;
import ru.kopanev.repository.impl.ProductRepositoryImpl;
import ru.kopanev.repository.impl.UserRepositoryImpl;
import ru.kopanev.service.*;
import ru.kopanev.service.impl.AuditServiceImpl;
import ru.kopanev.service.impl.AuthServiceImpl;
import ru.kopanev.service.impl.ProductServiceImpl;
import ru.kopanev.ui.MenuUi;
import ru.kopanev.utils.ProductCache;
import ru.kopanev.utils.UserSession;

/**
 * Фабрика для создания и настройки всех компонентов приложения.
 * Реализует паттерн Factory для инициализации зависимостей и связывания
 * компонентов системы (репозитории, сервисы, UI).
 *
 * <p>Порядок инициализации:</p>
 * <ol>
 *   <li>Вспомогательные компоненты (UserSession, ProductCache)</li>
 *   <li>Репозитории (ProductRepository, UserRepository, EventRepository)</li>
 *   <li>Сервисы (AuditService, AuthService, ProductService)</li>
 *   <li>UI (MenuUi)</li>
 * </ol>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public class ApplicationFactory {

    /**
     * Создаёт и настраивает все компоненты приложения.
     * Инициализирует зависимости в правильном порядке и возвращает
     * готовый к использованию главный UI компонент.
     *
     * @return настроенный экземпляр MenuUi с внедрёнными зависимостями
     */
    public MenuUi createApplication() {
        UserSession session = createUserSession();
        ProductCache cache = createProductCache();

        ProductRepository productRepository = createProductRepository();
        UserRepository userRepository = createUserRepository();
        EventRepository eventRepository = createEventRepository();

        AuditService auditService = createAuditService(eventRepository);
        AuthService authService = createAuthService(auditService, userRepository, session);
        ProductService productService = createProductService(productRepository, auditService, cache);

        return createMenuUi(authService, productService, auditService, session);
    }

    /**
     * Создаёт репозиторий для работы с пользователями.
     * @return экземпляр UserRepository
     */
    private UserRepository createUserRepository() {
        return new UserRepositoryImpl();
    }

    /**
     * Создаёт репозиторий для работы с событиями аудита.
     * @return экземпляр EventRepository
     */
    private EventRepository createEventRepository() {
        return new EventRepositoryImpl();
    }

    /**
     * Создаёт сервис аудита с внедрённым репозиторием событий.
     * @param eventRepository репозиторий событий
     * @return экземпляр AuditService
     */
    private AuditService createAuditService(EventRepository eventRepository) {
        return new AuditServiceImpl(eventRepository);
    }

    /**
     * Создаёт сервис аутентификации с внедрёнными зависимостями.
     * @param auditService сервис аудита
     * @param userRepository репозиторий пользователей
     * @param session сессия пользователя
     * @return экземпляр AuthService
     */
    private AuthService createAuthService(AuditService auditService, UserRepository userRepository, UserSession session) {
        return new AuthServiceImpl(auditService, userRepository, session);
    }

    /**
     * Создаёт репозиторий для работы с товарами.
     * @return экземпляр ProductRepository
     */
    private ProductRepository createProductRepository() {
        return new ProductRepositoryImpl();
    }

    /**
     * Создаёт сервис товаров с внедрёнными зависимостями.
     * @param repository репозиторий товаров
     * @param auditService сервис аудита
     * @param cache кэш товаров
     * @return экземпляр ProductService
     */
    private ProductService createProductService(ProductRepository repository, AuditService auditService, ProductCache cache) {
        return new ProductServiceImpl(repository, auditService, cache);
    }

    /**
     * Создаёт сессию пользователя для отслеживания состояния входа.
     * @return экземпляр UserSession
     */
    private UserSession createUserSession() {
        return new UserSession();
    }

    /**
     * Создаёт кэш товаров для оптимизации чтения.
     * @return экземпляр ProductCache
     */
    private ProductCache createProductCache() {
        return new ProductCache();
    }

    /**
     * Создаёт главное меню приложения с внедрёнными зависимостями.
     * @param authService сервис аутентификации
     * @param productService сервис товаров
     * @param auditService сервис аудита
     * @param session сессия пользователя
     * @return экземпляр MenuUi
     */
    private MenuUi createMenuUi(AuthService authService, ProductService productService, AuditService auditService, UserSession session) {
        return new MenuUi(authService, productService, auditService, session);
    }
}
