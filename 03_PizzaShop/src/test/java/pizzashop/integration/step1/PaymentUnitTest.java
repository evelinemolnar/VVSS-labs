package pizzashop.integration.step1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

public class PaymentUnitTest {
    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment(10, PaymentType.Cash, 25.5);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_getTableNumber() {
        Assertions.assertEquals(10, payment.getTableNumber());
    }

    @Test
    void test_getPaymentType() {
        Assertions.assertEquals(PaymentType.Cash, payment.getType());
    }

    @Test
    void test_getAmount() {
        Assertions.assertEquals(25.5, payment.getAmount());
    }

    @Test
    void test_setTableNumber() {
        payment.setTableNumber(15);
        Assertions.assertEquals(15, payment.getTableNumber());
    }

    @Test
    void test_setPaymentType() {
        payment.setType(PaymentType.Card);
        Assertions.assertEquals(PaymentType.Card, payment.getType());
    }

    @Test
    void test_setAmount() {
        payment.setAmount(31.1);
        Assertions.assertEquals(31.1, payment.getAmount());
    }

}
