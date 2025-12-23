package com.Ferdyano.frontend.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Item: Base class untuk semua item yang dapat dikumpulkan dalam game.
 * (Misalnya: Artifact Key, Food, Water).
 */

public class Item {

    private final String name;
    private final String type; // "FRUIT" atau "WATER"
    private final String description;
    private float x, y;
    private Texture texture;
    private Rectangle bounds;

    // PERBAIKAN: Menambahkan 'type' ke dalam parameter konstruktor
    public Item(String name, String type, String description, float x, float y, Texture texture) {
        this.name = name;
        this.type = type; // Sekarang 'type' mendapatkan nilai (FRUIT/WATER)
        this.description = description;
        this.x = x;
        this.y = y;
        this.texture = texture;

        // Inisialisasi kotak tabrakan (disesuaikan menjadi 50x50 agar pas dengan render)
        this.bounds = new Rectangle(x, y, 50, 50);
    }

    // --- GETTER ---

    public String getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}


