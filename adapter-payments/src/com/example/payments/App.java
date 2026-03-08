package com.example.payments;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Map<String, PaymentGateway> gateways = new HashMap<>();
        // TODO: register adapters instead of raw SDKs
        gateways.put("fastpay", new FastPayAdaptor(new FastPayClient()));
        gateways.put("safecash", new SafeCashAdaptor(new SafeCashClient()));
//        OrderService svc = new OrderService(gateways);

        OrderService id1 = new OrderService(gateways.get("fastpay"));
        id1.processOrder("cust-1" , 5000);
        OrderService id2 = new OrderService(gateways.get("safecash"));
        id2.processOrder("cust-2" , 3000);

    }
}
