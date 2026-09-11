package com.example.demo.strategy;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DiscountContext {

    private final Map<String, DiscountStrategy> strategies;

    public DiscountContext(Map<String, DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    private DiscountStrategy resolve(String discountType) {
        if (discountType == null || discountType.isEmpty()) {
            return strategies.get("none");
        }
        return strategies.getOrDefault(discountType.toLowerCase(), strategies.get("none"));
    }

    public double applyDiscount(double price, String discountType) {
        return resolve(discountType).calculateDiscount(price);
    }

    public String getDisplayName(String discountType) {
        return resolve(discountType).getDisplayName();
    }
}