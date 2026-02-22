package services;

import entities.OrderLine;

import java.util.List;

public class StaffDiscountRule implements DiscountRule {
    public double compute(double subtotal, List<OrderLine> lines) {
        return lines.size() >= 3 ? 15.0 : 5.0;
    }
}
