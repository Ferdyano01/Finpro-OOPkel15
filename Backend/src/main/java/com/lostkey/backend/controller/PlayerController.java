package com.lostkey.backend.controller;

import com.lostkey.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    public record RegisterRequest(String username) {}

    public record SaveRequest(String checkpointId, int hp, int hunger, int thirst, int foodCount, boolean bossDefeated) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest player) {
        try {
            return ResponseEntity.ok(playerService.createPlayer(player.username()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/save")
    public ResponseEntity<?> saveGame(@PathVariable UUID id, @RequestBody SaveRequest req) {
        try {
            return ResponseEntity.ok(playerService.saveProgress(
                    id, req.checkpointId(), req.hp(), req.hunger(), req.thirst(), req.foodCount(), req.bossDefeated()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable UUID id) {
        return ResponseEntity.ok(playerService.getPlayer(id));
    }
}