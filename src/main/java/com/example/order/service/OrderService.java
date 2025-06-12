package main.java.com.example.order.service;

import main.java.com.example.order.exception.InsufficientInventoryException;
import main.java.com.example.order.model.Order;
import main.java.com.example.order.model.OrderStatus;
import main.java.com.example.order.repository.OrderRepository;
import main.java.com.example.order.util.DiscountCalculator;

public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final DiscountCalculator discountCalculator;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, InventoryService inventoryService,
                        DiscountCalculator discountCalculator, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.discountCalculator = discountCalculator;
        this.paymentService = paymentService;
    }

    public boolean placeOrder(Order order) {
        try {
            inventoryService.checkAndReduceInventory(order.getProducts());

            double total = order.getProducts().stream()
                    .mapToDouble(p -> p.getPrice() * p.getQuantity())
                    .sum();
            order.setTotalPrice(total);

            double discount = discountCalculator.calculateDiscount(order);
            order.setDiscount(discount);

            boolean paymentSuccess = paymentService.processPayment(order, order.calculateFinalPrice());
            if (!paymentSuccess) {
                order.setStatus(OrderStatus.CANCELLED);
                return false;
            }

            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.saveOrder(order);

            return true;
        } catch (InsufficientInventoryException e) {
            order.setStatus(OrderStatus.CANCELLED);
            return false;
        }
    }
}
