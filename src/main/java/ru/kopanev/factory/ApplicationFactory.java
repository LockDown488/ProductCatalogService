package ru.kopanev.factory;

import ru.kopanev.repository.*;
import ru.kopanev.service.*;
import ru.kopanev.ui.MenuUi;
import ru.kopanev.utils.ProductCache;
import ru.kopanev.utils.UserSession;

public class ApplicationFactory {
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

    private UserRepository createUserRepository() {
        return new UserRepositoryImpl();
    }

    private EventRepository createEventRepository() {
        return new EventRepositoryImpl();
    }

    private AuditService createAuditService(EventRepository eventRepository) {
        return new AuditServiceImpl(eventRepository);
    }

    private AuthService createAuthService(AuditService auditService, UserRepository userRepository, UserSession session) {
        return new AuthServiceImpl(auditService, userRepository, session);
    }

    private ProductRepository createProductRepository() {
        return new ProductRepositoryImpl();
    }

    private ProductService createProductService(ProductRepository repository, AuditService auditService, ProductCache cache) {
        return new ProductServiceImpl(repository, auditService, cache);
    }

    private UserSession createUserSession() {
        return new UserSession();
    }

    private ProductCache createProductCache() {
        return new ProductCache();
    }

    private MenuUi createMenuUi(AuthService authService, ProductService productService, AuditService auditService, UserSession session) {
        return new MenuUi(authService, productService, auditService, session);
    }
}
