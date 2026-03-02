package pricing;

import entities.Money;
import enums.AddOn;

public interface AddOnPricing {
    boolean supports(AddOn addOn);

    Money getPrice();
}
