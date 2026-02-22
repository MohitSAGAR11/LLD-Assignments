package services;

import dtos.Invoice;
import entities.MenuItem;
import entities.OrderLine;
import printer.ConsolePrinter;
import printer.InvoiceFormatter;
import repository.InvoiceRepository;
import java.util.List;
import java.util.Map;

public class CafeteriaSystem {
    private final BillingService billing;
    private final InvoiceFormatter formatter;
    private final ConsolePrinter printer;
    private final InvoiceRepository repository;
    private final Map<String, MenuItem> menu;
    private int invoiceSeq = 1000;

    public CafeteriaSystem(Map<String, MenuItem> menu,
                           BillingService billing,
                           InvoiceFormatter formatter,
                           ConsolePrinter printer,
                           InvoiceRepository repository) {
        this.menu = menu;
        this.billing = billing;
        this.formatter = formatter;
        this.printer = printer;
        this.repository = repository;
    }

    public void checkout(List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);
        Invoice invoice = billing.compute(invId, lines);
        List<String> formatted = formatter.format(invoice, menu);
        printer.print(formatted);
        repository.save(invoice, formatted);
        System.out.println("Saved invoice: " + invId +
                " (lines=" + repository.countLines(invId) + ")");
    }
}