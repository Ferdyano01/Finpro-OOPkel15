package com.Ferdyano.frontend.core;

/**
 * Item: Base class untuk semua item yang dapat dikumpulkan dalam game.
 * (Misalnya: Artifact Key, Food, Water).
 */
public class Item {

    private final String name;
    private final String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter yang dibutuhkan oleh InventoryManager
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
