package com.lostkey.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "player_scores")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "high_score")
    private int highScore = 0;

    public Score() {}

    // Getters and Setters
    public Long getId() { return id; }

    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
    
    // Helper to update only if new score is better
    public void updateScoreIfHigher(int newScore) {
        if (newScore > this.highScore) {
            this.highScore = newScore;
        }
    }
}