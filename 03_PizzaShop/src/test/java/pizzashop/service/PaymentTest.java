package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.exceptions.PaymentException;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() throws PaymentException, IOException {
        MenuRepository menuRepository = new MenuRepository("data/menu.txt");
        PaymentRepository paymentRepository = new PaymentRepository("data/payments.txt");
        pizzaService = new PizzaService(menuRepository, paymentRepository);
    }

    @Test
    void getTotalAmountPaymentTypeEqualsTest() {
        PaymentType paymentType = PaymentType.Card;
        Payment payment = new Payment(1, paymentType, 100.0f);
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        double result = pizzaService.getTotalAmount(paymentList, paymentType);

        assertEquals(100.0f, result);
    }

    @Test
    void getTotalAmountPaymentTypeNotEqualsTest() {
        PaymentType paymentType = PaymentType.Card;
        Payment payment = new Payment(1, paymentType, 100.0f);
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        double result = pizzaService.getTotalAmount(paymentList, PaymentType.Cash);

        assertEquals(0.0f, result);
    }

    @Test
    void getTotalAmountEmptyListTest() {
        PaymentType paymentType = PaymentType.Card;
        List<Payment> paymentList = new ArrayList<>();
        double result = pizzaService.getTotalAmount(paymentList, paymentType);

        assertEquals(0.0f, result);
    }

    @Test
    void getTotalAmountNullListTest() {
        PaymentType paymentType = PaymentType.Card;
        double result = pizzaService.getTotalAmount(null, paymentType);

        assertEquals(0.0f, result);
    }

    @Test
    void getTotalAmountPaymentTypeNotAlwaysEqualTest() {
        PaymentType paymentType = PaymentType.Card;
        Payment payment1 = new Payment(1, PaymentType.Card, 100.0f);
        Payment payment2 = new Payment(1, PaymentType.Cash, 100.0f);
        List<Payment> paymentList = new ArrayList<>();

        paymentList.add(payment1);
        paymentList.add(payment2);

        double result = pizzaService.getTotalAmount(paymentList, paymentType);

        assertEquals(100.0f, result);
    }
}
