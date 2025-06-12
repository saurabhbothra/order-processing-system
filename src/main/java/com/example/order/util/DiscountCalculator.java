package main.java.com.example.order.util;

import main.java.com.example.order.model.Order;
import main.java.com.example.order.model.Product;

public class DiscountCalculator {

    public double calculateDiscount(Order order) {
        double discount = 0;
        for (Product product : order.getProducts()) {
            if (product.getQuantity() > 5) {
                discount += product.getPrice() * 0.1;
            }
        }
        return discount;
    }
}
