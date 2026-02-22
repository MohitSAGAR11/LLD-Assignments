package services;

import entities.OrderLine;

import java.util.List;

public interface DiscountRule {
    double compute(double subtotal , List<OrderLine> lines);
}
