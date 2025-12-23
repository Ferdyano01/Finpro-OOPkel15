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

    // 2. Modules
    // CascadeType.ALL ensures when saving or deleting a Player,
    // it automatically saves or deletes the corresponding variable such as health, inventory, etc.

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "health_id", referencedColumnName = "id")
    private Health health = new Health();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    private Inventory inventory = new Inventory();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "score_id", referencedColumnName = "id")
    private Score score = new Score();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "progress_id", referencedColumnName = "id")
    private GameProgress gameProgress = new GameProgress();

    // 3. Metadata
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Player() {}

    public Player(String username) {
        this.username = username;
    }

    // Getters and Setters
    public UUID getPlayerId() { return playerId; }
    public void setPlayerId(UUID playerId) { this.playerId = playerId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Health getHealth() { return health; }
    public void setHealth(Health health) { this.health = health; }

    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public Score getScore() { return score; }
    public void setScore(Score score) { this.score = score; }

    public GameProgress getGameProgress() { return gameProgress; }
    public void setGameProgress(GameProgress gameProgress) { this.gameProgress = gameProgress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}