package com.annapoorna.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "inventory_items")
public class InventoryItem {

    @Id
    private String id;

    private String name;
    private String description;
    private int quantity;
    private double price;
    private boolean inStock;
    //private String[] categories;
    @Field("category")
    private String category;
    private String imageUrl;
    private String vendorId;
    private long lastUpdatedTimestamp; // Timestamp in milliseconds

}
