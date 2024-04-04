package pizzashop.service;

import pizzashop.exceptions.MenuException;
import pizzashop.exceptions.PaymentException;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;

public class PizzaService {

    private MenuRepository menuRepo;
    private PaymentRepository payRepo;

    public PizzaService(MenuRepository menuRepo, PaymentRepository payRepo) {
        this.menuRepo = menuRepo;
        this.payRepo = payRepo;
    }

    public List<MenuDataModel> getMenuData() throws MenuException {
        return menuRepo.getMenu();
    }

    public List<Payment> getPayments() {
        return payRepo.getAll();
    }

    public void addPayment(int table, String type, double amount) throws PaymentException {
        if (table < 1 || table > 8)
            throw new PaymentException("Invalid table");

        if (!isPaymentTypeValid(type)) {
            throw new PaymentException("Invalid type");
        }
        PaymentType paymentType = PaymentType.valueOf(type);

        if (amount <= 0) throw new PaymentException("Invalid amount");

        Payment payment = new Payment(table, paymentType, amount);
        payRepo.add(payment);
    }

    private boolean isPaymentTypeValid(String type) {
        try {
            PaymentType.valueOf(type);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public double getTotalAmount(PaymentType type) {
        double total = 0.0f;
        List<Payment> l = getPayments();
        if ((l == null) || (l.size() == 0)) return total;
        for (Payment p : l) {
            if (p.getType().equals(type))
                total += p.getAmount();
        }
        return total;
    }

}