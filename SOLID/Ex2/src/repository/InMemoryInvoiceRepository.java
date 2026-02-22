package repository;

import dtos.Invoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryInvoiceRepository implements InvoiceRepository {
    private final Map<String, List<String>> invoices = new HashMap<>();
    @Override
    public void save(Invoice invoice, List<String> lines) {
        invoices.put(invoice.invoiceNumber , lines);
    }

    @Override
    public int countLines(String invoiceNumber) {
        List<String> lines = invoices.get(invoiceNumber);
        return lines == null ? 0 : lines.size();
    }
}
