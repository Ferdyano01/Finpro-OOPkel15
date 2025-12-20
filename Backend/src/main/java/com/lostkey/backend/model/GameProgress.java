package com.lostkey.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "player_progress")
public class GameProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_checkpoint_id")
    private String lastCheckpointId = "start_area";

    @Column(name = "is_boss_defeated")
    private boolean isBossDefeated = false;

    public GameProgress() {}

    // Getters and Setters
    public Long getId() { return id; }

    public String getLastCheckpointId() { return lastCheckpointId; }
    public void setLastCheckpointId(String lastCheckpointId) { this.lastCheckpointId = lastCheckpointId; }

    public boolean isBossDefeated() { return isBossDefeated; }
    public void setBossDefeated(boolean bossDefeated) { isBossDefeated = bossDefeated; }
}