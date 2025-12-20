package com.lostkey.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "player_health")
public class Health {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_hp")
    private int currentHp = 100;

    @Column(name = "hunger_level")
    private int hungerLevel = 100;

    @Column(name = "thirst_level")
    private int thirstLevel = 100;

    public Health() {}

    // Getters and Setters
    public Long getId() { return id; }
    
    public int getCurrentHp() { return currentHp; }
    public void setCurrentHp(int currentHp) { this.currentHp = currentHp; }

    public int getHungerLevel() { return hungerLevel; }
    public void setHungerLevel(int hungerLevel) { this.hungerLevel = hungerLevel; }

    public int getThirstLevel() { return thirstLevel; }
    public void setThirstLevel(int thirstLevel) { this.thirstLevel = thirstLevel; }
}