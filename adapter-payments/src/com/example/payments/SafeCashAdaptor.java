package com.example.payments;

public class SafeCashAdaptor implements PaymentGateway{
    private final SafeCashClient client;

    public SafeCashAdaptor(SafeCashClient client) {
        this.client = client;
    }

    @Override
    public String charge(String customerId, int amountCents) {
        SafeCashPayment payment = client.createPayment(amountCents , customerId);
        return payment.confirm();
    }
}
