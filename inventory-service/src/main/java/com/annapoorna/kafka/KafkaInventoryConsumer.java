package com.annapoorna.kafka;

import com.annapoorna.entity.InventoryItem;
import com.annapoorna.repository.InventoryItemRepository;
import com.annapoorna.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaInventoryConsumer {

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = "inventory-topic", groupId = "inventory-group")
    public void listen(InventoryItem inventoryItem) {
        // Access fields of the inventory item
        String itemId = inventoryItem.getId();
        String itemName = inventoryItem.getName();
        int itemQuantity = inventoryItem.getQuantity();

        // Perform business logic based on the inventory item data
        if (itemQuantity < 10) {
            // Trigger reordering process if quantity is low
            reorderInventoryItem(itemId, itemName);
        }

        // Update database with inventory item details
        updateInventoryItemInDatabase(inventoryItem);

        // Log the processing result
        System.out.println("Inventory item processed successfully: " + inventoryItem);
    }

    private void reorderInventoryItem(String itemId, String itemName) {
        // Logic to trigger reordering process
        System.out.println("Low quantity for item " + itemName + ". Triggering reordering process...");
        // Invoke external service, send notification, etc.
    }

    private void updateInventoryItemInDatabase(InventoryItem inventoryItem) {
        // Logic to update inventory item in database
        inventoryService.updateInventoryItem(inventoryItem.getId(), inventoryItem);
        // Example: inventoryRepository.save(inventoryItem);
        System.out.println("Updating inventory item in database: " + inventoryItem);
    }
}

