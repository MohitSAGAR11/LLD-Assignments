import entities.OrderLine;
import printer.ConsolePrinter;
import printer.InvoiceFormatter;
import repository.InMemoryInvoiceRepository;
import services.*;
import util.MenuLoader;

import java.util.List;
import java.util.Map;
import entities.MenuItem;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Cafeteria Billing ===");

        Map<String, MenuItem> menu = MenuLoader.load();

        // Student order
        TaxRule studentTax = new StudentTaxRule();
        DiscountRule studentDiscount = new StudentDiscountRule();
        BillingService billing = new BillingService(menu, studentTax, studentDiscount);
        CafeteriaSystem sys = new CafeteriaSystem(
                menu, billing,
                new InvoiceFormatter(),
                new ConsolePrinter(),
                new InMemoryInvoiceRepository()
        );

        List<OrderLine> order = List.of(
                new OrderLine("M1", 2),
                new OrderLine("C1", 1)
        );
        sys.checkout(order);
    }
}