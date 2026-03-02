package pricing;

import entities.Money;

public interface RoomPricing {
    boolean supports(int roomType);

    Money getBasePrice();
}
