package com.annapoorna.service;

import com.annapoorna.entity.InventoryItem;
import com.annapoorna.kafka.KafkaInventoryProducer;
import com.annapoorna.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryItemRepository repository;

    @Autowired
    private KafkaInventoryProducer kafkaInventoryProducer;

    public List<InventoryItem> getAllInventoryItems() {
        return repository.findAll();
    }

    public InventoryItem getInventoryItemById(String id) {
        return repository.findById(id).orElse(null);
    }

    public InventoryItem createInventoryItem(InventoryItem item) {
        InventoryItem createdItem =  repository.save(item);
        kafkaInventoryProducer.sendMessage(item); // Send inventory item details to Kafka
        return createdItem;
    }

    public boolean deleteItemById(String id) {
        repository.deleteById(id);
        return false;
    }

    public List<InventoryItem> getItemsByName(String name) {
        return repository.findByName(name);
    }

    public List<InventoryItem> getItemsByCategory(String category) {
        return repository.findByCategory(category);
    }

    public InventoryItem updateInventoryItem(String id, InventoryItem updatedInventoryItem) {
        InventoryItem existingItem = repository.findById(id).orElse(null);
        if (existingItem != null) {
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
            InventoryItem updatedItem = repository.save(existingItem);

            // Send updated inventory item details to Kafka
            kafkaInventoryProducer.sendMessage(updatedItem);

            return updatedItem;
        } else {
            // Item with the specified ID not found, return null
            return null;
        }
    }

    public void updateItemPrice(String id, double price) {
        InventoryItem item = repository.findById(id).orElse(null);
        if (item != null) {
            item.setPrice(price);
            repository.save(item);
            kafkaInventoryProducer.sendMessage(item); // Send updated inventory item details to Kafka
        }
    }

    public void restockItem(String id, int quantity) {
        InventoryItem item = repository.findById(id).orElse(null);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            repository.save(item);
            kafkaInventoryProducer.sendMessage(item); // Send updated inventory item details to Kafka
        }
    }

    public void sellItem(String id, int quantity) {
        InventoryItem item = repository.findById(id).orElse(null);
        if (item != null) {
            int availableQuantity = item.getQuantity();
            if (availableQuantity >= quantity) {
                item.setQuantity(availableQuantity - quantity);
                repository.save(item);
                kafkaInventoryProducer.sendMessage(item); // Send updated inventory item details to Kafka
            } else {
                // Handle insufficient inventory
                throw new IllegalArgumentException("Insufficient quantity available for item: " + item.getName());
            }
        }
    }

    // Other methods based on your business requirements
}
