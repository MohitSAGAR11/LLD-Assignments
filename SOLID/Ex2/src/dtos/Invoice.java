package dtos;

import entities.OrderLine;

import java.util.List;

public class Invoice {
    public final String invoiceNumber;
    public final List<OrderLine> lines;
    public final double subtotal;
    public final double tax;
    public final double taxPct;
    public final double discount;
    public final double total;

    public Invoice(String invoiceNumber, List<OrderLine> lines, double subtotal, double tax, double taxPct, double discount, double total) {
        this.invoiceNumber = invoiceNumber;
        this.lines = lines;
        this.subtotal = subtotal;
        this.tax = tax;
        this.taxPct = taxPct;
        this.discount = discount;
        this.total = total;
    }
}
