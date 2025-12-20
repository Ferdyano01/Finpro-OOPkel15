package com.lostkey.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player_inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_count")
    private int foodCount = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "inventory_keys", joinColumns = @JoinColumn(name = "inventory_id"))
    @Column(name = "key_name")
    private List<String> artifactKeys = new ArrayList<>();

    public Inventory() {}

    // Getters and Setters
    public Long getId() { return id; }

    public int getFoodCount() { return foodCount; }
    public void setFoodCount(int foodCount) { this.foodCount = foodCount; }

    public List<String> getArtifactKeys() { return artifactKeys; }
    public void setArtifactKeys(List<String> artifactKeys) { this.artifactKeys = artifactKeys; }
}