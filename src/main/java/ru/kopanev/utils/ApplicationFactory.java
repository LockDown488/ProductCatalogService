package ru.kopanev.utils;

import ru.kopanev.repository.ProductRepository;
import ru.kopanev.repository.ProductRepositoryImpl;
import ru.kopanev.service.AuditService;
import ru.kopanev.service.AuthService;
import ru.kopanev.service.ProductService;
import ru.kopanev.service.ProductServiceImpl;
import ru.kopanev.ui.MenuUi;

public class ApplicationFactory {
    public MenuUi createApplication() {
        AuditService auditService = createAuditService();
        AuthService authService = createAuthService(auditService);
        ProductRepository productRepository = createProductRepository();
        ProductService productService = createProductService(productRepository, auditService);

        return createMenuUi(authService, productService, auditService);
    }

    private AuditService createAuditService() {
        return new AuditService();
    }

    private AuthService createAuthService(AuditService auditService) {
        return new AuthService(auditService);
    }

    private ProductRepository createProductRepository() {
        return new ProductRepositoryImpl();
    }

    private ProductService createProductService(ProductRepository repository, AuditService auditService) {
        return new ProductServiceImpl(repository, auditService);
    }

    private MenuUi createMenuUi(AuthService authService, ProductService productService, AuditService auditService) {
        return new MenuUi(authService, productService, auditService);
    }
}
