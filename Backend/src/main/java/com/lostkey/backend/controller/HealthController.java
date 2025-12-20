package com.lostkey.backend.controller;

import com.lostkey.backend.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updateHealth(@PathVariable UUID playerId, @RequestBody HealthRequest request) {
        return ResponseEntity.ok(healthService.updateHealth(playerId, request.hp, request.hunger, request.thirst));
    }

    public static class HealthRequest {
        public int hp;
        public int hunger;
        public int thirst;
    }
}