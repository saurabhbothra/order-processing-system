package main.java.com.example.order.repository;

import main.java.com.example.order.model.Order;

import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository {
    private final ConcurrentHashMap<String, Order> orderDB = new ConcurrentHashMap<>();

    public void saveOrder(Order order) {
        orderDB.put(order.getOrderId(), order);
    }

    public Order findOrderById(String orderId) {
        return orderDB.get(orderId);
    }
}
