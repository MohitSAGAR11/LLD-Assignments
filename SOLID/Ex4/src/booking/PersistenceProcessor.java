package booking;

import dto.BookingRequest;
import entities.Money;
import repository.FakeBookingRepo;

import java.util.Random;

public class PersistenceProcessor implements BookingProcessor {

    private static final int BASE_BOOKINGS_COUNT = 7000;
    private static final int RANDOM_SEED = 1;
    private static final int BOUNDING_RADIUS = 1000;

    private final FakeBookingRepo repo;

    public PersistenceProcessor(FakeBookingRepo repo) {
        this.repo = repo;
    }

    public void process(BookingRequest req, Money monthly, Money deposit) {
        String bookingId = "H-" + (BASE_BOOKINGS_COUNT + new Random(RANDOM_SEED).nextInt(BOUNDING_RADIUS));
        repo.save(bookingId, req, monthly, deposit);
    }
}