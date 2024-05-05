package pizzashop.integration.step1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.exceptions.PaymentException;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class PaymentRepositoryUnitTest {
    private PaymentRepository paymentRepository;
    private List<Payment> initialPayments;

    @BeforeEach
    void setUp() throws PaymentException, IOException {
        initialPayments = new ArrayList<>(Arrays.asList(
                new Payment(1, PaymentType.Card, 12.0),
                new Payment(2, PaymentType.Cash, 22.4)
        ));
        paymentRepository = Mockito.spy(new PaymentRepository("test_payments.txt"));
        Mockito.doReturn(initialPayments).when(paymentRepository).getAll();
    }

    @Test
    void test_getAll() {
        List<Payment> payments = paymentRepository.getAll();
        Assertions.assertEquals(2, payments.size());
        Assertions.assertEquals(new Payment(1, PaymentType.Card, 12.0), payments.get(0));
        Assertions.assertEquals(new Payment(2, PaymentType.Cash, 22.4), payments.get(1));
    }

    @Test
    void test_add() throws PaymentException, IOException {
        Payment payment = new Payment(3, PaymentType.Card, 10.0);

        Mockito.doAnswer(invocation -> {
            Payment arg = invocation.getArgument(0);
            initialPayments.add(arg);
            return null;
        }).when(paymentRepository).add(any(Payment.class));

        paymentRepository.add(payment);

        List<Payment> updatedPayments = paymentRepository.getAll();

        Assertions.assertEquals(3, updatedPayments.size());
        Assertions.assertEquals(payment, updatedPayments.get(2));

        Mockito.verify(paymentRepository).add(payment);
    }
}

