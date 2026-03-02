package pricing;

import entities.Money;
import enums.LegacyRoomTypes;

public class DeluxeRoomPricing implements RoomPricing {
    private static final double DELUXE_ROOM_PRICE = 16000.0;

    @Override
    public boolean supports(int roomType) {
        return LegacyRoomTypes.nameOf(roomType).equals(LegacyRoomTypes.nameOf(LegacyRoomTypes.DELUXE));
    }

    @Override
    public Money getBasePrice() {
        return new Money(DELUXE_ROOM_PRICE);
    }
}
