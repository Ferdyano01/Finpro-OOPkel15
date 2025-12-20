package com.lostkey.backend.controller;

import com.lostkey.backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updateInventory(@PathVariable UUID playerId, @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(playerId, request.foodCount, request.artifactKeys));
    }

    public static class InventoryRequest {
        public int foodCount;
        public List<String> artifactKeys;
    }
}