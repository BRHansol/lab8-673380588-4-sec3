package com.example.demo.strategy;

import org.springframework.stereotype.Component;

@Component("none")
public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(double price) {
        return price;
    }
    @Override
    public String getDisplayName() {
        return "ราคาปกติ";
    }
}