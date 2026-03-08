package com.example.payments;

import java.util.Map;
import java.util.Objects;

public class OrderService {
    private final PaymentGateway gateway;

    public OrderService(PaymentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway, "gateway");
    }

    // Smell: still switches; your refactor should remove this by ensuring map contains adapters.
//    public String charge(String provider, String customerId, int amountCents) {
//        Objects.requireNonNull(provider, "provider");
//        PaymentGateway gw = gateways.get(provider);
//        if (gw == null) throw new IllegalArgumentException("unknown provider: " + provider);
//        return gw.charge(customerId, amountCents);
//    }

    public void processOrder(String customerId, int amountCents) {
        String txId = gateway.charge(customerId, amountCents); // clean, simple, done
        System.out.println("Transaction ID: " + txId);
    }

}
