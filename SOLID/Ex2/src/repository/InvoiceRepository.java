package repository;

import dtos.Invoice;

import java.util.List;

public interface InvoiceRepository {
    void save(Invoice invoice , List<String> lines);
    int countLines(String invoiceNumber);
}
