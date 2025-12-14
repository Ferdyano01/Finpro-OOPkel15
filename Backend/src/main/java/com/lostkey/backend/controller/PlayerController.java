package com.lostkey.backend.controller;

import com.lostkey.backend.model.Player;
import com.lostkey.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // 1. Register & Login
    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        try {
            Player createdPlayer = playerService.createPlayer(player);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getPlayerByUsername(@PathVariable String username) {
        Optional<Player> player = playerService.getPlayerByUsername(username);
        if (player.isPresent()) {
            return ResponseEntity.ok(player.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Player not found: " + username + "\"}");
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        boolean exists = playerService.isUsernameExists(username);
        return ResponseEntity.ok("{\"exists\": " + exists + "}");
    }

    // 2. Saving Progress
    // This is the specific endpoint for saving system.
    // Use a helper class SaveRequest to organize the JSON data cleanly.
    @PutMapping("/{playerId}/save")
    public ResponseEntity<?> saveGameProgress(@PathVariable UUID playerId, @RequestBody SaveRequest request) {
        try {
            Player updatedPlayer = playerService.saveGameProgress(
                    playerId,
                    request.checkpointId,
                    request.hp,
                    request.hunger,
                    request.thirst,
                    request.bossDefeated
            );
            return ResponseEntity.ok(updatedPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // 3. Updating Profile
    @PutMapping("/{playerId}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable UUID playerId, @RequestBody Player player) {
        try {
            Player updatedPlayer = playerService.updatePlayerProfile(playerId, player);
            return ResponseEntity.ok(updatedPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // 4. Leaderboard
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Player>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        // We only have one leaderboard of high score for this game type
        List<Player> leaderboard = playerService.getLeaderboardByHighScore();
        return ResponseEntity.ok(leaderboard);
    }

    // 5. Cleanup
    @DeleteMapping("/{playerId}")
    public ResponseEntity<?> deletePlayer(@PathVariable UUID playerId) {
        try {
            playerService.deletePlayer(playerId);
            return ResponseEntity.ok("{\"message\": \"Player deleted successfully\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // Helper class
    // This allows the controller to read the JSON sent specifically for saving the game
    public static class SaveRequest {
        public String checkpointId;
        public int hp;
        public int hunger;
        public int thirst;
        public boolean bossDefeated;
    }
}