package pricing;

import entities.Money;
import enums.LegacyRoomTypes;

public class TripleRoomPricing implements RoomPricing {
    private static final double TRIPLE_ROOM_PRICE = 12000.0;

    @Override
    public boolean supports(int roomType) {
        return LegacyRoomTypes.nameOf(roomType).equals(LegacyRoomTypes.nameOf(LegacyRoomTypes.TRIPLE));
    }

    @Override
    public Money getBasePrice() {
        return new Money(TRIPLE_ROOM_PRICE);
    }
}
