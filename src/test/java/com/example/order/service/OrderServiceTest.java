package test.java.com.example.order.service;

import main.java.com.example.order.exception.InsufficientInventoryException;
import main.java.com.example.order.model.Order;
import main.java.com.example.order.model.OrderStatus;
import main.java.com.example.order.model.Product;
import main.java.com.example.order.repository.InventoryRepository;
import main.java.com.example.order.repository.OrderRepository;
import main.java.com.example.order.service.InventoryService;
import main.java.com.example.order.service.OrderService;
import main.java.com.example.order.service.PaymentService;
import main.java.com.example.order.util.DiscountCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private OrderService orderService;
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setup() {
        inventoryRepository = new InventoryRepository();
        OrderRepository orderRepository = new OrderRepository();
        InventoryService inventoryService = new InventoryService(inventoryRepository);
        DiscountCalculator discountCalculator = new DiscountCalculator();
        PaymentService paymentService = new PaymentService();

        orderService = new OrderService(orderRepository, inventoryService, discountCalculator, paymentService);
    }

    @Test
    public void testPlaceOrder_Successful() {
        Product p1 = new Product("P100", "Product 1", 100.0, 6);
        Product p2 = new Product("P300", "Product 3", 75.0, 4);

        Order order = new Order("O123", Arrays.asList(p1, p2));

        boolean result = orderService.placeOrder(order);

        assertTrue(result, "Order should be placed successfully");

        double expectedTotal = 100 * 6 + 75 * 4;
        assertEquals(expectedTotal, order.getTotalPrice(), 0.01);

        double expectedDiscount = 100 * 6 * 0.1; // Only p1 qualifies
        assertEquals(expectedDiscount, order.getDiscount(), 0.01);

        assertEquals(OrderStatus.COMPLETED, order.getStatus());

        assertEquals(order.getTotalPrice() - order.getDiscount(), order.calculateFinalPrice(), 0.01);

        // Optionally verify inventory reduced (if accessible)
        int remainingInventoryP100 = inventoryRepository.getQuantity("P100");
        assertEquals(10 - 6, remainingInventoryP100);
    }

    @Test
    public void testPlaceOrder_ExactInventoryQuantity() {
        Product p1 = new Product("P200", "Product 2", 50.0, 3); // Exactly matches inventory

        Order order = new Order("O124", Arrays.asList(p1));

        boolean result = orderService.placeOrder(order);

        assertTrue(result, "Order should succeed when quantity matches inventory exactly");
        assertEquals(OrderStatus.COMPLETED, order.getStatus());

        int remainingInventory = inventoryRepository.getQuantity("P200");
        assertEquals(0, remainingInventory);
    }

    @Test
    public void testInventoryReduction_AfterOrder() throws InsufficientInventoryException {
        InventoryRepository inventoryRepository = new InventoryRepository();
        InventoryService inventoryService = new InventoryService(inventoryRepository);

        Product product = new Product("P100", "Product 1", 100.0, 2);

        // Check initial inventory
        int initialQty = inventoryRepository.getQuantity("P100");
        assertEquals(10, initialQty);

        // Reduce inventory
        inventoryService.checkAndReduceInventory(Collections.singletonList(product));

        int afterQty = inventoryRepository.getQuantity("P100");
        assertEquals(initialQty - 2, afterQty);
    }

    @Test
    public void testOrderStatusIsCompletedAfterSaving() {
        // Arrange
        Product product = new Product("P100", "Product 1", 100.0, 2);
        Order order = new Order("O500", List.of(product));

        OrderRepository orderRepository = new OrderRepository();
        InventoryRepository inventoryRepository = new InventoryRepository();
        InventoryService inventoryService = new InventoryService(inventoryRepository);
        DiscountCalculator discountCalculator = new DiscountCalculator();
        PaymentService paymentService = new PaymentService();

        OrderService orderService = new OrderService(orderRepository, inventoryService, discountCalculator, paymentService);

        // Act
        boolean result = orderService.placeOrder(order);

        // Assert
        assertTrue(result, "Order placement should succeed");

        // Retrieve the saved order from repository
        Order savedOrder = orderRepository.findOrderById(order.getOrderId());
        assertNotNull(savedOrder, "Saved order should not be null");

        // Verify order status is COMPLETED
        assertEquals(OrderStatus.COMPLETED, savedOrder.getStatus(), "Order status should be COMPLETED after saving");
    }

    @Test
    public void testPlaceOrder_InsufficientInventory() {
        Product p1 = new Product("P200", "Product 2", 50.0, 5); // More than inventory

        Order order = new Order("O125", Arrays.asList(p1));

        boolean result = orderService.placeOrder(order);

        assertFalse(result, "Order should fail due to insufficient inventory");
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }
}
