package com.example.demo.strategy;

import org.springframework.stereotype.Component;

@Component("member")
public class MemberDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(double price) {
        return price * 0.9;
    }
    @Override
    public String getDisplayName() {
        return "ส่วนลดสมาชิก 10%";
    }
}