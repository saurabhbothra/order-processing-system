package main.java.com.example.order.service;

import main.java.com.example.order.exception.InsufficientInventoryException;
import main.java.com.example.order.model.Product;
import main.java.com.example.order.repository.InventoryRepository;

import java.util.List;

public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public synchronized void checkAndReduceInventory(List<Product> products) throws InsufficientInventoryException {
        for (Product product : products) {
            int availableQty = inventoryRepository.getQuantity(product.getProductId());
            if (availableQty < product.getQuantity()) {
                throw new InsufficientInventoryException("Insufficient inventory for product " + product.getProductId());
            }
        }
    }

}
