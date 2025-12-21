package com.Ferdyano.frontend.ui;

// --- Import yang benar ---
import com.Ferdyano.frontend.pattern.InventoryObserver;
import com.Ferdyano.frontend.Managers.InventoryManager;
import com.Ferdyano.frontend.core.Item;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.graphics.Color; // Wajib import Color

import java.util.List;

public class InventoryBar extends Table implements InventoryObserver {

    private final Label foodCountLabel;
    private final Image[] keySlots;
    private final Skin skin;
    private final Drawable keyFoundDrawable;

    // --- VARIABEL BARU UNTUK MENGHINDARI CRASH ---
    private final Drawable frameBackgroundDrawable;
    private final Drawable emptySlotDrawable;
    // ----------------------------------------------

    public InventoryBar(Skin skin) {
        this.skin = skin;
        this.keySlots = new Image[5];

        // --- Mendapatkan Placeholder/Fallback Drawables ---

        // KOREKSI KRITIS 1: Buat Drawable Latar Belakang secara langsung
        // Ini adalah Drawable yang akan menggantikan "inventory_frame"
        this.frameBackgroundDrawable = skin.newDrawable("white", Color.DARK_GRAY);

        // Placeholder untuk Ikon
        this.emptySlotDrawable = skin.newDrawable("white", Color.GRAY); // Slot abu-abu
        this.keyFoundDrawable = skin.newDrawable("white", Color.YELLOW); // Kunci kuning terang
        Drawable foodIconDrawable = skin.newDrawable("white", Color.GREEN); // Makanan hijau

        // 1. Inisialisasi Label
        this.foodCountLabel = new Label("x0", skin, "default");

        // --- Layout ---
        bottom().right();

        // KOREKSI KRITIS 2: Menggunakan drawable lokal yang PASTI ADA
        setBackground(frameBackgroundDrawable); // Menggantikan skin.getDrawable("inventory_frame")

        // Slot kunci
        for (int i = 0; i < keySlots.length; i++) {
            // KOREKSI 3: Menggunakan emptySlotDrawable
            keySlots[i] = new Image(emptySlotDrawable);
            keySlots[i].setVisible(true);
            add(keySlots[i]).size(50, 50).pad(5);
        }

        // Slot makanan
        add(new Image(foodIconDrawable)).size(40, 40).padLeft(20);
        add(foodCountLabel).padRight(10);

        // Register observer
        InventoryManager.getInstance().registerObserver(this);
        System.out.println("InventoryBar registered to InventoryManager.");
    }

    // --- Implementasi interface InventoryObserver ---
    @Override
    public void updateInventory(List<Item> artifactKeys, int foodCount) {

        foodCountLabel.setText("x" + foodCount);

        // Update key slot display
        for (int i = 0; i < keySlots.length; i++) {
            if (i < artifactKeys.size()) {
                // Menggunakan keyFoundDrawable (kuning)
                keySlots[i].setDrawable(keyFoundDrawable);
                keySlots[i].setVisible(true);
            } else {
                // Menggunakan emptySlotDrawable (abu-abu)
                keySlots[i].setDrawable(emptySlotDrawable);
            }
        }
    }
}

