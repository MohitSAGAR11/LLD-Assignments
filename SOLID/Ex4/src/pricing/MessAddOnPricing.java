package pricing;
import entities.Money;
import enums.AddOn;

public class MessAddOnPricing implements AddOnPricing {

    private static final double MESS_ADD_ON_PRICE = 1000.0;

    @Override
    public boolean supports(AddOn addOn) {
        return addOn.equals(AddOn.MESS);
    }

    @Override
    public Money getPrice() {
        return new Money(MESS_ADD_ON_PRICE);
    }
}