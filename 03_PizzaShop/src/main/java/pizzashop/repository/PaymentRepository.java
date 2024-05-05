package pizzashop.repository;

import pizzashop.exceptions.PaymentException;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class PaymentRepository implements IPaymentRepository {
    private static String filename;
    private List<Payment> paymentList;

    public PaymentRepository(String filename) throws IOException, PaymentException {
        this.filename = filename;
        this.paymentList = new ArrayList<>();
        try {
            readPayments(); // Attempt to read payments
        } catch (PaymentException e) {
            System.err.println("Failed to read payments, starting with empty list: " + e.getMessage());
        }
    }

    private void readPayments() throws PaymentException {
        //ClassLoader classLoader = PaymentRepository.class.getClassLoader();
        File file = new File(filename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line=br.readLine())!=null){
                Payment payment=getPayment(line);
                paymentList.add(payment);
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new PaymentException("Could not find the payment file.", e);
        } catch (IOException e) {
            throw new PaymentException("An error occurred while reading the payment.", e);
        }
    }

    private Payment getPayment(String line) throws PaymentException {
        if (line == null || line.equals("")) return null;
        StringTokenizer st = new StringTokenizer(line, ",");
        try {
            int tableNumber = Integer.parseInt(st.nextToken());
            String type = st.nextToken().trim();
            double amount = Double.parseDouble(st.nextToken());
            return new Payment(tableNumber, PaymentType.valueOf(type), amount);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw new PaymentException("Error parsing payment information: " + line, e);
        }
    }

    public void add(Payment payment) throws PaymentException {
        paymentList.add(payment);
    }

    public List<Payment> getAll(){
        return paymentList;
    }

    public void writeAll() throws PaymentException {
        //ClassLoader classLoader = PaymentRepository.class.getClassLoader();
        File file = new File(filename);

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            for (Payment p:paymentList) {
                System.out.println(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new PaymentException("An error occurred while writing the payment.", e);
        }
    }

}