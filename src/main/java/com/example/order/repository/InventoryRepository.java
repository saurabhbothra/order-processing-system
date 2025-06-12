package main.java.com.example.order.repository;

import java.util.concurrent.ConcurrentHashMap;

public class InventoryRepository {
    private final ConcurrentHashMap<String, Integer> inventory = new ConcurrentHashMap<>();

    public InventoryRepository() {
        inventory.put("P100", 10);
        inventory.put("P200", 3);
        inventory.put("P300", 20);
    }

    public int getQuantity(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    public void reduceQuantity(String productId, int amount) {
        inventory.computeIfPresent(productId, (k, v) -> v - amount);
    }
}
