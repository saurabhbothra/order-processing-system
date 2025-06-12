package main.java.com.example.order.model;

import java.util.List;

public class Order {
    private final String orderId;
    private final List<Product> products;
    private double totalPrice;
    private double discount;
    private OrderStatus status;

    public Order(String orderId, List<Product> products) {
        this.orderId = orderId;
        this.products = products;
        this.status = OrderStatus.NEW;
    }

    // Getters and setters

    public String getOrderId() { return orderId; }
    public List<Product> getProducts() { return products; }
    public double getTotalPrice() { return totalPrice; }
    public double getDiscount() { return discount; }
    public OrderStatus getStatus() { return status; }

    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setDiscount(double discount) { this.discount = discount; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public double calculateFinalPrice() {
        return totalPrice - discount;
    }
}
