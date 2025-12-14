package com.lostkey.backend.service;

import com.lostkey.backend.model.Player;
import com.lostkey.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    // 1. CRUD (Create, Read, Delete)
    public Player createPlayer(Player player) {
        if (playerRepository.existsByUsername(player.getUsername())) {
            throw new RuntimeException("Username already exists: " + player.getUsername());
        }
        // Initialize default survival stats just in case
        player.setCurrentHp(100);
        player.setHungerLevel(100);
        player.setLastCheckpointId("start_area");

        return playerRepository.save(player);
    }

    public Optional<Player> getPlayerById(UUID playerId) {
        return playerRepository.findById(playerId);
    }

    // Used for Login
    public Optional<Player> getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    public void deletePlayer(UUID playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new RuntimeException("Player not found with ID: " + playerId);
        }
        playerRepository.deleteById(playerId);
    }

    // 2. Game save
    public Player saveGameProgress(UUID playerId, String checkpointId, int hp, int hunger, int thirst, boolean bossDefeated) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        // Update location
        if (checkpointId != null && !checkpointId.isEmpty()) {
            player.setLastCheckpointId(checkpointId);
        }

        // Update survival stats
        player.setCurrentHp(hp);
        player.setHungerLevel(hunger);
        player.setThirstLevel(thirst);

        // Update boss status
        if (bossDefeated) {
            player.setBossDefeated(true);
        }

        return playerRepository.save(player);
    }

     // Update username or high score
    public Player updatePlayerProfile(UUID playerId, Player updatedDetails) {
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        // 1. Update Username
        if (updatedDetails.getUsername() != null) {
            if (!existingPlayer.getUsername().equals(updatedDetails.getUsername())
                    && playerRepository.existsByUsername(updatedDetails.getUsername())) {
                throw new RuntimeException("Username already exists: " + updatedDetails.getUsername());
            }
            existingPlayer.setUsername(updatedDetails.getUsername());
        }

        // 2. Update High Score
        if (updatedDetails.getHighScore() > 0) {
            existingPlayer.updateHighScore(updatedDetails.getHighScore());
        }

        return playerRepository.save(existingPlayer);
    }

    // 3. Leaderboards
    public List<Player> getLeaderboardByHighScore() {
        return playerRepository.findTop10ByOrderByHighScoreDesc();
    }

    public boolean isUsernameExists(String username) {
        return playerRepository.existsByUsername(username);
    }
}
