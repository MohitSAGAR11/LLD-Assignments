package utils;

public interface InPaymentGateway {
    String charge(String studentId, double amount);
}
