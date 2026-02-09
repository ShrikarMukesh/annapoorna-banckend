package com.annapoorna.service;

import com.annapoorna.entity.InventoryItem;
import com.annapoorna.exception.InsufficientInventoryException;
import com.annapoorna.exception.ResourceNotFoundException;
import com.annapoorna.kafka.KafkaInventoryProducer;
import com.annapoorna.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryItemRepository repository;

    @Autowired
    private KafkaInventoryProducer kafkaInventoryProducer;

    @Autowired
    private AuditLogService auditLogService;

    public List<InventoryItem> getAllInventoryItems() {
        log.info("Fetching all inventory items");
        List<InventoryItem> items = repository.findAll();

        auditLogService.logSuccess(
                "READ",
                "InventoryItem",
                null,
                null,
                null,
                null,
                "Fetched all inventory items. Count: " + items.size(),
                null);

        return items;
    }

    public InventoryItem getInventoryItemById(String id) {
        log.info("Fetching inventory item with id: {}", id);
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        auditLogService.logSuccess(
                "READ",
                "InventoryItem",
                id,
                null,
                null,
                null,
                "Inventory item retrieved successfully",
                null);

        return item;
    }

    public InventoryItem createInventoryItem(InventoryItem item) {
        log.info("Creating new inventory item: {}", item.getName());
        InventoryItem createdItem = repository.save(item);
        kafkaInventoryProducer.sendMessage(item);

        auditLogService.logSuccess(
                "CREATE",
                "InventoryItem",
                createdItem.getId(),
                null,
                null,
                createdItem,
                "Inventory item created successfully",
                null);

        return createdItem;
    }

    public void deleteItemById(String id) {
        log.info("Deleting inventory item with id: {}", id);
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        repository.deleteById(id);

        auditLogService.logSuccess(
                "DELETE",
                "InventoryItem",
                id,
                null,
                item,
                null,
                "Inventory item '" + item.getName() + "' deleted successfully",
                null);
    }

    public List<InventoryItem> getItemsByName(String name) {
        log.info("Fetching inventory items by name: {}", name);
        return repository.findByName(name);
    }

    public List<InventoryItem> getItemsByCategory(String category) {
        log.info("Fetching inventory items by category: {}", category);
        return repository.findByCategory(category);
    }

    public InventoryItem updateInventoryItem(String id, InventoryItem updatedInventoryItem) {
        log.info("Updating inventory item with id: {}", id);
        InventoryItem existingItem = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        // Capture old state for audit
        InventoryItem oldItem = new InventoryItem();
        oldItem.setId(existingItem.getId());
        oldItem.setName(existingItem.getName());
        oldItem.setQuantity(existingItem.getQuantity());
        oldItem.setPrice(existingItem.getPrice());

        // Update the fields of the existing item with the values from the updated item
        existingItem.setName(updatedInventoryItem.getName());
        existingItem.setDescription(updatedInventoryItem.getDescription());
        existingItem.setQuantity(updatedInventoryItem.getQuantity());
        existingItem.setPrice(updatedInventoryItem.getPrice());
        existingItem.setInStock(updatedInventoryItem.isInStock());
        existingItem.setCategory(updatedInventoryItem.getCategory());
        existingItem.setImageUrl(updatedInventoryItem.getImageUrl());
        existingItem.setVendorId(updatedInventoryItem.getVendorId());
        existingItem.setLastUpdatedTimestamp(updatedInventoryItem.getLastUpdatedTimestamp());

        // Save the updated item to the repository
        InventoryItem savedItem = repository.save(existingItem);

        // Send updated inventory item details to Kafka
        kafkaInventoryProducer.sendMessage(savedItem);

        auditLogService.logSuccess(
                "UPDATE",
                "InventoryItem",
                id,
                null,
                oldItem,
                savedItem,
                "Inventory item updated successfully",
                null);

        return savedItem;
    }

    public void updateItemPrice(String id, double price) {
        log.info("Updating price for inventory item with id: {}", id);
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        double oldPrice = item.getPrice();
        item.setPrice(price);
        repository.save(item);
        kafkaInventoryProducer.sendMessage(item);

        auditLogService.logSuccess(
                "UPDATE",
                "InventoryItem",
                id,
                null,
                "Price: " + oldPrice,
                "Price: " + price,
                "Inventory item price updated from " + oldPrice + " to " + price,
                null);
    }

    public void restockItem(String id, int quantity) {
        log.info("Restocking inventory item with id: {} by quantity: {}", id, quantity);
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        int oldQuantity = item.getQuantity();
        item.setQuantity(item.getQuantity() + quantity);
        repository.save(item);
        kafkaInventoryProducer.sendMessage(item);

        auditLogService.logSuccess(
                "RESTOCK",
                "InventoryItem",
                id,
                null,
                "Quantity: " + oldQuantity,
                "Quantity: " + item.getQuantity(),
                "Restocked " + quantity + " units. Total: " + item.getQuantity(),
                null);
    }

    public void sellItem(String id, int quantity) {
        log.info("Selling {} units of inventory item with id: {}", quantity, id);
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        int availableQuantity = item.getQuantity();
        if (availableQuantity >= quantity) {
            item.setQuantity(availableQuantity - quantity);
            repository.save(item);
            kafkaInventoryProducer.sendMessage(item);

            auditLogService.logSuccess(
                    "SELL",
                    "InventoryItem",
                    id,
                    null,
                    "Quantity: " + availableQuantity,
                    "Quantity: " + item.getQuantity(),
                    "Sold " + quantity + " units. Remaining: " + item.getQuantity(),
                    null);
        } else {
            throw new InsufficientInventoryException(
                    "Insufficient quantity available for item: " + item.getName() +
                            ". Requested: " + quantity + ", Available: " + availableQuantity);
        }
    }
}
