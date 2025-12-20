package com.lostkey.backend.service;

import com.lostkey.backend.model.*;
import com.lostkey.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player createPlayer(String username) {
        if (playerRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        Player newPlayer = new Player(username);
        return playerRepository.save(newPlayer);
    }

    public Player getPlayer(UUID playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }
}