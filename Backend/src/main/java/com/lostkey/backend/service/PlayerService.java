package com.lostkey.backend.service;

import com.lostkey.backend.model.*;
import com.lostkey.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player createPlayer(String username) {
    Optional<Player> name = playerRepository.findByUsername(username);
        if (name.isPresent()) {
            return name.get();
        } else {
            return playerRepository.save(new Player(username));
        }
    }

    public Player getPlayer(UUID playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    public Player saveProgress(UUID playerId, String checkpoint, int hp, int hunger, int thirst, int food, boolean bossDefeated) {
        Player player = getPlayer(playerId);

        // Update Health
        player.getHealth().setCurrentHp(hp);
        player.getHealth().setHungerLevel(hunger);
        player.getHealth().setThirstLevel(thirst);

        // Update Inventory
        player.getInventory().setFoodCount(food);

        // Update Progress
        player.getGameProgress().setLastCheckpointId(checkpoint);
        player.getGameProgress().setBossDefeated(bossDefeated);

        return playerRepository.save(player);
    }
}