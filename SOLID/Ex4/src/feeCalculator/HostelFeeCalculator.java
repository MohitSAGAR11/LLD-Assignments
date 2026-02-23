package feeCalculator;

import booking.BookingProcessor;
import booking.PersistenceProcessor;
import booking.ReceiptPrintingProcessor;
import dto.BookingRequest;
import entities.Money;
import enums.AddOn;

import pricing.*;
import repository.FakeBookingRepo;

import java.util.List;

public class HostelFeeCalculator {
    private final List<RoomPricing> roomPricing;
    private final List<AddOnPricing> addOnPricing;
    private final List<BookingProcessor> processors;
    private static final double BASE_MONEY_DEPOSIT_AMOUNT = 5000.0;

    public HostelFeeCalculator(FakeBookingRepo repo) {
        this.roomPricing = List.of(
                new SingleRoomPricing(),
                new DoubleRoomPricing(),
                new TripleRoomPricing(),
                new DeluxeRoomPricing()
        );
        this.addOnPricing = List.of(
                new GymAddOnPricing(),
                new LaundryAddOnPricing(),
                new MessAddOnPricing()
        );
        this.processors = List.of(
                new ReceiptPrintingProcessor(),
                new PersistenceProcessor(repo)
        );
    }

    public void process(BookingRequest req) {
        Money monthly = calculateMonthly(req);
        Money deposit = new Money(BASE_MONEY_DEPOSIT_AMOUNT);
        processors.forEach(p -> p.process(req, monthly, deposit));
    }

    private Money calculateMonthly(BookingRequest req) {
        double base = roomPricing.getLast().getBasePrice().amount;

        for (RoomPricing room : roomPricing) {
            if (room.supports(req.roomType)) {
                base = room.getBasePrice().amount;
                break;
            }
        }

        double addOns = 0.0;

        for (AddOn addOn : req.addOns) {
            for (AddOnPricing addOnPricing : addOnPricing) {
                if (addOnPricing.supports(addOn)) {
                    addOns += addOnPricing.getPrice().amount;
                    break;
                }
            }
        }

        return new Money(base + addOns);
    }
}