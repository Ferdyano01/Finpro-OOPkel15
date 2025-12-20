package com.lostkey.backend.controller;

import com.lostkey.backend.model.Player;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Player player) {
        try {
            return ResponseEntity.ok(playerService.createPlayer(player.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable UUID id) {
        return ResponseEntity.ok(playerService.getPlayer(id));
    }
}