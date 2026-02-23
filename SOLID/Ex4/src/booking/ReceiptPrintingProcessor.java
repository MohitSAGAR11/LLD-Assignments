package booking;

import dto.BookingRequest;
import entities.Money;
import printer.ReceiptPrinter;

public class ReceiptPrintingProcessor implements BookingProcessor {

    @Override
    public void process(BookingRequest req, Money monthly, Money deposit) {
        ReceiptPrinter.print(req, monthly, deposit);
    }
}