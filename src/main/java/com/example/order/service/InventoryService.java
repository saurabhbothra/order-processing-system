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

    public void checkAvailability(Product product) throws InsufficientInventoryException {
        int availableQty = inventoryRepository.getQuantity(product.getProductId());
        if (availableQty >= product.getQuantity()) {
            return;
        }
        throw new InsufficientInventoryException("Insufficient inventory for product " + product.getProductId());
    }

    public void reduceInventory(Product product) {
        inventoryRepository.reduceQuantity(product.getProductId(), product.getQuantity());
    }

    public synchronized void checkAndReduceInventory(List<Product> products) throws InsufficientInventoryException {
        for (Product product : products) {
            int availableQty = inventoryRepository.getQuantity(product.getProductId());
            if (availableQty < product.getQuantity()) {
                throw new InsufficientInventoryException("Insufficient inventory for product " + product.getProductId());
            }
        }
        for (Product product : products) {
            inventoryRepository.reduceQuantity(product.getProductId(), product.getQuantity());
        }
    }

}
