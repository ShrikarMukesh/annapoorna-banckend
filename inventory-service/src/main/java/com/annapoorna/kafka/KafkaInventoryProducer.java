package com.annapoorna.kafka;

import com.annapoorna.entity.InventoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaInventoryProducer {
    private static final String TOPIC = "inventory-topic";

    @Autowired
    private KafkaTemplate<String, InventoryItem> kafkaTemplate;

    public void sendMessage(InventoryItem item) {
        kafkaTemplate.send(TOPIC, item);
    }
}
