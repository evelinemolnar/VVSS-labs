package pizzashop.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pizzashop.exceptions.PaymentException;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTest {
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() throws PaymentException {
        MenuRepository menuRepository = new MenuRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        pizzaService = new PizzaService(menuRepository, paymentRepository);
    }

    @Test
    @DisplayName("Test valid payment with Cash")
    void testValidPaymentWithCash() {
        // Arrange
        int table = 1;
        String type = "Cash";
        double amount = 100.0;

        // Act & Assert
        assertDoesNotThrow(() -> pizzaService.addPayment(table, type, amount),
                "A valid payment with type 'Cash' and amount 100 should not throw exception.");
    }

    @Test
    @DisplayName("Test payment with invalid type")
    void testPaymentWithInvalidType() {
        // Arrange
        int table = 1;
        String type = "";
        double amount = 100.0;

        // Act & Assert
        Exception exception = assertThrows(PaymentException.class, () -> pizzaService.addPayment(table, type, amount),
                "An invalid type should throw a PaymentException.");

        // Verify that the exception message matches the expected error
        assertTrue(exception.getMessage().contains("Invalid type"));
    }

    @Test
    @DisplayName("Test payment with non-numeric amount")
    @Disabled("This test case is not applicable as the code should not compile if the amount is not a double.")
    void testPaymentWithNonNumericAmount() {
        // Arrange
        int table = 1;
        String type = "Cash";
        String amount = "100q"; // Invalid amount, should be a double not a string

        // Act & Assert
        // In this case, the code should not compile if the amount is not a double,
        // hence this test case should be checked by the compiler rather than at runtime.
        // Java is a statically-typed language, so the test case "amount is not number"
        // is not applicable at runtime as it would be a compile-time error.
    }

    @Test
    @DisplayName("Test valid payment with Card")
    void testValidPaymentWithCard() {
        // Arrange
        int table = 1;
        String type = "Card";
        double amount = 100.0;

        // Act & Assert
        assertDoesNotThrow(() -> pizzaService.addPayment(table, type, amount),
                "A valid payment with type 'Card' and amount 100 should not throw exception.");
    }

    @ParameterizedTest
    @DisplayName("Test valid payment with valid amount")
    @ValueSource(doubles = {0.01, 50.0, 100.0})
    void testValidPaymentWithValidAmounts(double amount) {
        // Arrange
        int table = 1;
        String type = "Cash";

        // Act & Assert
        assertDoesNotThrow(() -> pizzaService.addPayment(table, type, amount),
                "A valid payment with type 'Cash' and amount 0.01 should not throw exception.");
    }

    @RepeatedTest(3)
    @DisplayName("Test invalid payment with invalid amount")
    void testInvalidPaymentWithInvalidAmount() {
        // Arrange
        int table = 1;
        String type = "Cash";
        double amount = 0;

        // Act & Assert
        Exception exception = assertThrows(PaymentException.class, () -> pizzaService.addPayment(table, type, amount),
                "An invalid amount should throw a PaymentException.");

        // Verify that the exception message matches the expected error
        assertTrue(exception.getMessage().contains("Invalid amount"));
    }
}