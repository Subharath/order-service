package com.savoryswift.orderservice;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    @Autowired
    private MongoClient mongoClient;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        System.out.println(".....Server is running on port 7002.....");

        try {
            MongoDatabase db = mongoClient.getDatabase("savory-orders");
            db.listCollectionNames().first(); // simple read to ensure connection works
            System.out.println(".....MongoDB connected successfully to 'savory-orders'.....");
        } catch (Exception e) {
            System.err.println("MongoDB connection failed: " + e.getMessage());
        }
    }
}
