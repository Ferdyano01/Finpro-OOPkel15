package com.lostkey.backend.service;

import com.lostkey.backend.model.Inventory;
import com.lostkey.backend.model.Player;
import com.lostkey.backend.repository.InventoryRepository;
import com.lostkey.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory updateInventory(UUID playerId, int foodCount, List<String> artifactKeys) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Inventory inventory = player.getInventory();
        inventory.setFoodCount(foodCount);
        // Only update keys if a list is provided
        if (artifactKeys != null) {
            inventory.setArtifactKeys(artifactKeys);
        }

        return inventoryRepository.save(inventory);
    }
}