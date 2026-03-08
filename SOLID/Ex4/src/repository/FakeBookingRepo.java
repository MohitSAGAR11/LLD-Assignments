package repository;

import dto.BookingRequest;
import entities.Money;

public class FakeBookingRepo {
    public void save(String id, BookingRequest req, Money monthly, Money deposit) {
        System.out.println("Saved booking: " + id);
    }
}
