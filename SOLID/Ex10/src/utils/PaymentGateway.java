package utils;

public class PaymentGateway implements InPaymentGateway {
    public String charge(String studentId, double amount) {
        // fake deterministic txn
        return "TXN-9001";
    }
}
