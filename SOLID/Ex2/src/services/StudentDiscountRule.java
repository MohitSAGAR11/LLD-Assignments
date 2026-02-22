package services;

import entities.OrderLine;

import java.util.List;

public class StudentDiscountRule implements DiscountRule {
    public double compute(double subtotal, List<OrderLine> lines) {
        return subtotal >= 180.0 ? 10.0 : 0.0;
    }
}
