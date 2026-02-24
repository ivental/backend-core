package ru.mentee.power.crm.spring.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndFindProduct_whenValidData() {
        Product product = new Product();
        product.setName("Консультация по архитектуре");
        product.setSku("CONSULT-ARCH-001");
        product.setPrice(new BigDecimal("50000.00"));
        product.setActive(true);
        Product saved = productRepository.save(product);
        assertThat(saved.getId()).isNotNull();
        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getSku()).isEqualTo("CONSULT-ARCH-001");
    }

    @Test
    void shouldFindBySku_whenProductExists() {
        Product product = new Product();
        product.setName("Ноутбук ASUS X707II");
        product.setSku("ASUS-X-707-II");
        product.setPrice(new BigDecimal("99990.00"));
        product.setActive(true);
        productRepository.save(product);

        // When
        Optional<Product> found = productRepository.findBySku("ASUS-X-707-II");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ноутбук ASUS X707II");
    }

    @Test
    void shouldFindByActiveTrue_whenProductsExist() {
        // Given
        Product laptop = new Product();
        laptop.setName("Монитор Tuoshuo 32 NanoIps");
        laptop.setSku("TUOSHUO-32-NANOIPS");
        laptop.setPrice(new BigDecimal("38990"));
        laptop.setActive(true);
        productRepository.save(laptop);

        Product service = new Product();
        service.setName("Быстросервис (год)");
        service.setSku("QUICK-SERVICE-YEAR");
        service.setPrice(new BigDecimal("14400.00"));
        service.setActive(true);
        productRepository.save(service);

        Product discontinued = new Product();
        discontinued.setName("Клавиатура Cidoo V75 Pro");
        discontinued.setSku("CIDOO-V75-PRO");
        discontinued.setPrice(new BigDecimal("7000.00"));
        discontinued.setActive(false);
        productRepository.save(discontinued);

        // When
        List<Product> activeProducts = productRepository.findByActiveTrue();

        // Then
        assertThat(activeProducts).hasSize(2);
        assertThat(activeProducts)
                .extracting(Product::getSku)
                .containsExactlyInAnyOrder("TUOSHUO-32-NANOIPS", "QUICK-SERVICE-YEAR");
    }

    @Test
    void shouldEnforceUniqueSkuConstraint() {
        Product original = new Product();
        original.setName("Геймпад 8bitDo M30");
        original.setSku("8-BITDO-M30");
        original.setPrice(new BigDecimal("2000"));
        original.setActive(true);
        productRepository.save(original);
        Product duplicate = new Product();
        duplicate.setName("Геймпад 8bitDo M30 (уценка)");
        duplicate.setSku("8-BITDO-M30");
        duplicate.setPrice(new BigDecimal("1500"));
        duplicate.setActive(true);
        assertThatThrownBy(() -> {
            productRepository.save(duplicate);
            productRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
