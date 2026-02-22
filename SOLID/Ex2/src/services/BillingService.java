package services;

import dtos.Invoice;
import entities.MenuItem;
import entities.OrderLine;

import java.util.List;
import java.util.Map;

public class BillingService {
    private final Map<String , MenuItem> menu;
    private final TaxRule taxRule;
    private final DiscountRule discountRule;

    public BillingService(Map<String, MenuItem> menu, TaxRule taxRule, DiscountRule discountRule) {
        this.menu = menu;
        this.taxRule = taxRule;
        this.discountRule = discountRule;
    }

    public Invoice compute(String invoiceNumber , List<OrderLine> lines) {
        double subtotal = 0.0;
        for (OrderLine line : lines) {
            subtotal += menu.get(line.itemId).price * line.qty;
        }
        double taxPct = taxRule.taxPercent();
        double tax = subtotal * (taxPct / 100.0);
        double discount = discountRule.compute(subtotal , lines);
        double total = subtotal + tax - discount;
        return new Invoice(invoiceNumber , lines , subtotal , taxPct , tax , discount , total);
    }
}
