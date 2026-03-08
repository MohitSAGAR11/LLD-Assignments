package printer;

import dtos.Invoice;
import entities.MenuItem;
import entities.OrderLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvoiceFormatter {
    public List<String> format(Invoice inv , Map<String , MenuItem> menu) {
        List<String> out = new ArrayList<>();
        out.add("Invoice# " + inv.invoiceNumber);
        for (OrderLine line : inv.lines) {
            MenuItem item = menu.get(line.itemId);
            out.add(String.format("- %s x%d = %.2f" , item.name , line.qty , item.price * line.qty));
        }
        out.add(String.format("Subtotal: %.2f", inv.subtotal));
        out.add(String.format("Tax(%.0f%%): %.2f", inv.taxPct, inv.tax));
        out.add(String.format("Discount: -%.2f", inv.discount));
        out.add(String.format("TOTAL: %.2f", inv.total));
        return out;
    }
}
