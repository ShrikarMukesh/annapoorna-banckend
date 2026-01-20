package com.annapoorna.repository;

import com.annapoorna.entity.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InventoryItemRepository extends MongoRepository<InventoryItem, String> {
    List<InventoryItem> findByName(String name);

    List<InventoryItem> findByCategory(String category);
}
