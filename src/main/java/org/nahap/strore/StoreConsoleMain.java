package org.nahap.strore;

import org.nahap.strore.application.repository.DepartmentRepository;
import org.nahap.strore.application.repository.ProductRepository;
import org.nahap.strore.application.service.DepartmentService;
import org.nahap.strore.application.service.ProductService;
import org.nahap.strore.domain.model.Department;
import org.nahap.strore.infrastructure.memory.InMemoryDepartmentRepository;
import org.nahap.strore.infrastructure.memory.InMemoryProductRepository;
import org.nahap.strore.presentation.console.ConsoleMenu;

import java.math.BigDecimal;
import java.time.LocalTime;

public final class StoreConsoleMain {
    private StoreConsoleMain() {
    }

    public static void main(String[] args) {
        DepartmentRepository departmentRepository = new InMemoryDepartmentRepository();
        ProductRepository productRepository = new InMemoryProductRepository();
        DepartmentService departmentService = new DepartmentService(departmentRepository, productRepository);
        ProductService productService = new ProductService(productRepository, departmentRepository);
        seedData(departmentService, productService);
        ConsoleMenu consoleMenu = new ConsoleMenu(departmentService, productService);
        consoleMenu.start();
    }

    private static void seedData(DepartmentService departmentService, ProductService productService) {
        Department groceries = departmentService.createDepartment("Продукты", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Department electronics = departmentService.createDepartment("Электроника", LocalTime.of(10, 0), LocalTime.of(21, 0));
        Department books = departmentService.createDepartment("Книги", LocalTime.of(9, 0), LocalTime.of(20, 0));
        Department clothes = departmentService.createDepartment("Одежда", LocalTime.of(10, 0), LocalTime.of(22, 0));
        Department home = departmentService.createDepartment("Дом и кухня", LocalTime.of(9, 0), LocalTime.of(21, 0));
        Department sports = departmentService.createDepartment("Спорт", LocalTime.of(9, 0), LocalTime.of(21, 0));
        Department beauty = departmentService.createDepartment("Красота", LocalTime.of(10, 0), LocalTime.of(22, 0));
        Department kids = departmentService.createDepartment("Детские товары", LocalTime.of(9, 0), LocalTime.of(21, 0));
        Department pets = departmentService.createDepartment("Зоотовары", LocalTime.of(9, 0), LocalTime.of(21, 0));
        Department office = departmentService.createDepartment("Канцтовары", LocalTime.of(9, 0), LocalTime.of(20, 0));
        departmentService.createDepartment("Сад и дача", LocalTime.of(8, 0), LocalTime.of(20, 0));
        departmentService.createDepartment("Автотовары", LocalTime.of(9, 0), LocalTime.of(21, 0));

        productService.createProduct(groceries.getId(), "Молоко", new BigDecimal("89.90"));
        productService.createProduct(groceries.getId(), "Хлеб", new BigDecimal("54.00"));
        productService.createProduct(groceries.getId(), "Сыр", new BigDecimal("179.00"));
        productService.createProduct(groceries.getId(), "Яйца", new BigDecimal("99.90"));
        productService.createProduct(groceries.getId(), "Яблоки", new BigDecimal("129.00"));
        productService.createProduct(groceries.getId(), "Кофе", new BigDecimal("459.00"));

        productService.createProduct(electronics.getId(), "Наушники", new BigDecimal("2990.00"));
        productService.createProduct(electronics.getId(), "Мышь", new BigDecimal("1490.00"));
        productService.createProduct(electronics.getId(), "Клавиатура", new BigDecimal("3490.00"));
        productService.createProduct(electronics.getId(), "Монитор", new BigDecimal("12990.00"));
        productService.createProduct(electronics.getId(), "Смартфон", new BigDecimal("45990.00"));
        productService.createProduct(electronics.getId(), "Пауэрбанк", new BigDecimal("2590.00"));

        productService.createProduct(books.getId(), "Java Core", new BigDecimal("1200.00"));
        productService.createProduct(books.getId(), "Чистый код", new BigDecimal("1450.00"));
        productService.createProduct(books.getId(), "Алгоритмы", new BigDecimal("980.00"));
        productService.createProduct(books.getId(), "Паттерны проектирования", new BigDecimal("1750.00"));

        productService.createProduct(clothes.getId(), "Футболка", new BigDecimal("1290.00"));
        productService.createProduct(clothes.getId(), "Джинсы", new BigDecimal("3990.00"));
        productService.createProduct(clothes.getId(), "Куртка", new BigDecimal("6990.00"));
        productService.createProduct(clothes.getId(), "Кроссовки", new BigDecimal("5490.00"));
        productService.createProduct(clothes.getId(), "Носки", new BigDecimal("390.00"));

        productService.createProduct(home.getId(), "Сковорода", new BigDecimal("2190.00"));
        productService.createProduct(home.getId(), "Кастрюля", new BigDecimal("2890.00"));
        productService.createProduct(home.getId(), "Набор тарелок", new BigDecimal("1990.00"));
        productService.createProduct(home.getId(), "Чайник", new BigDecimal("2490.00"));
        productService.createProduct(home.getId(), "Плед", new BigDecimal("1790.00"));

        productService.createProduct(sports.getId(), "Гантели", new BigDecimal("3290.00"));
        productService.createProduct(sports.getId(), "Коврик для йоги", new BigDecimal("1490.00"));
        productService.createProduct(sports.getId(), "Футбольный мяч", new BigDecimal("1890.00"));
        productService.createProduct(sports.getId(), "Скакалка", new BigDecimal("590.00"));

        productService.createProduct(beauty.getId(), "Шампунь", new BigDecimal("490.00"));
        productService.createProduct(beauty.getId(), "Крем для лица", new BigDecimal("890.00"));
        productService.createProduct(beauty.getId(), "Зубная паста", new BigDecimal("220.00"));
        productService.createProduct(beauty.getId(), "Парфюм", new BigDecimal("3490.00"));

        productService.createProduct(kids.getId(), "Конструктор", new BigDecimal("2590.00"));
        productService.createProduct(kids.getId(), "Кукла", new BigDecimal("1390.00"));
        productService.createProduct(kids.getId(), "Детская книга", new BigDecimal("590.00"));
        productService.createProduct(kids.getId(), "Самокат", new BigDecimal("4990.00"));

        productService.createProduct(pets.getId(), "Корм для кошек", new BigDecimal("990.00"));
        productService.createProduct(pets.getId(), "Поводок", new BigDecimal("790.00"));
        productService.createProduct(pets.getId(), "Игрушка для собак", new BigDecimal("650.00"));

        productService.createProduct(office.getId(), "Тетрадь", new BigDecimal("120.00"));
        productService.createProduct(office.getId(), "Ручка", new BigDecimal("80.00"));
        productService.createProduct(office.getId(), "Маркер", new BigDecimal("150.00"));
        productService.createProduct(office.getId(), "Папка", new BigDecimal("210.00"));

        departmentService.updateDepartment(office.getId(), "Канцелярия и офис", LocalTime.of(9, 0), LocalTime.of(21, 0));
    }
}