package pizzashop.repository;

import pizzashop.exceptions.PaymentException;
import pizzashop.model.Payment;

import java.util.List;

public interface IPaymentRepository {
    void add(Payment payment) throws PaymentException;
    List<Payment> getAll();
}
