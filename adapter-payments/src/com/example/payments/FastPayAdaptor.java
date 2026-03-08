package com.example.payments;

public class FastPayAdaptor implements  PaymentGateway {
    private final FastPayClient client;

    public FastPayAdaptor(FastPayClient client) {
        this.client = client;
    }

    @Override
    public String charge(String customerId, int amountCents) {
        return client.payNow(customerId, amountCents);
    }
}
