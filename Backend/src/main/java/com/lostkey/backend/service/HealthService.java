package com.lostkey.backend.service;

import com.lostkey.backend.model.Health;
import com.lostkey.backend.model.Player;
import com.lostkey.backend.repository.HealthRepository;
import com.lostkey.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class HealthService {

    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private HealthRepository healthRepository;

    public Health updateHealth(UUID playerId, int hp, int hunger, int thirst) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Health health = player.getHealth();
        health.setCurrentHp(hp);
        health.setHungerLevel(hunger);
        health.setThirstLevel(thirst);

        return healthRepository.save(health);
    }
}