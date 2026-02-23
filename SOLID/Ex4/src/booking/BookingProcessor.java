package booking;

import dto.BookingRequest;
import entities.Money;

public interface BookingProcessor {
    void process(BookingRequest req , Money monthly , Money deposit);
}
