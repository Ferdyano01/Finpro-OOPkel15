package com.Ferdyano.frontend.pattern;

import com.Ferdyano.frontend.core.Item;

import java.util.List;

public interface InventoryObserver {
    // --- Implementasi dari InventoryObserver (METODE KUNCI) ---
    void updateInventory(List<Item> artifactKeys, int foodCount);



}
