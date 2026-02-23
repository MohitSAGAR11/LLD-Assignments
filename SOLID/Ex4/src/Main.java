import dto.BookingRequest;
import enums.AddOn;
import enums.LegacyRoomTypes;
import feeCalculator.HostelFeeCalculator;
import repository.FakeBookingRepo;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hostel Fee Calculator ===");
        BookingRequest req = new BookingRequest(LegacyRoomTypes.DOUBLE, List.of(AddOn.LAUNDRY, AddOn.MESS));
        HostelFeeCalculator calc = new HostelFeeCalculator(new FakeBookingRepo());
        calc.process(req);
    }
}