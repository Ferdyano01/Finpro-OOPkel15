package com.lostkey.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "players")
public class Player {

    // 1. Identity
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "player_id")
    private UUID playerId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "high_score")
    private int highScore = 0;

    // 2. Survival
    @Column(name = "current_hp")
    private int currentHp = 100;

    @Column(name = "hunger_level")
    private int hungerLevel = 100;

    @Column(name = "thirst_level")
    private int thirstLevel = 100;

    // 3. Progress
    @Column(name = "last_checkpoint_id")
    private String lastCheckpointId = "start_area";

    @Column(name = "is_boss_defeated")
    private boolean isBossDefeated = false;

    // 4. Metadata
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Player() {}

    public Player(String username) {
        this.username = username;
    }

    // Getter and Setter
    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public int getThirstLevel() {
        return thirstLevel;
    }

    public void setThirstLevel(int thirstLevel) {
        this.thirstLevel = thirstLevel;
    }

    public String getLastCheckpointId() {
        return lastCheckpointId;
    }

    public void setLastCheckpointId(String lastCheckpointId) {
        this.lastCheckpointId = lastCheckpointId;
    }

    public boolean isBossDefeated() {
        return isBossDefeated;
    }

    public void setBossDefeated(boolean bossDefeated) {
        isBossDefeated = bossDefeated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper Methods
    public void updateSurvivalStats(int hpChange, int hungerChange, int thirstChange) {
        this.currentHp = Math.max(0, Math.min(100, this.currentHp + hpChange));
        this.hungerLevel = Math.max(0, Math.min(100, this.hungerLevel + hungerChange));
        this.thirstLevel = Math.max(0, Math.min(100, this.thirstLevel + thirstChange));
    }

    public void updateHighScore(int newScore) {
        if (newScore > this.highScore) {
            this.highScore = newScore;
        }
    }
}
