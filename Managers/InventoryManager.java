package com.Ferdyano.frontend.Managers;

import com.Ferdyano.frontend.pattern.InventoryObserver; // Asumsi package architecture Anda
import com.Ferdyano.frontend.core.Item; // Asumsi Item base class ada di game/entities/
import java.util.ArrayList;
import java.util.List;

/**
 * InventoryManager: Singleton Class yang mengelola semua item (Kunci, Makanan) pemain.
 * Bertindak sebagai Subject (Pemberi Notifikasi) untuk InventoryObserver.
 */
public class InventoryManager {

    // --- Singleton Implementation ---
    private static final InventoryManager instance = new InventoryManager();

    // Konstruktor private
    private InventoryManager() {
        // Inisialisasi awal inventori
    }

    // Metode akses global
    public static InventoryManager getInstance() {
        return instance;
    }

    // --- Data Inti Game State Inventori ---

    private int foodCount = 0;
    private final List<Item> artifactKeys = new ArrayList<>();

    // --- Observer List ---

    private final List<InventoryObserver> observers = new ArrayList<>();

    // --- Observer Registration Methods ---

    /** Mendaftarkan komponen UI (misalnya InventoryBar) untuk menerima update inventori. */
    public void registerObserver(InventoryObserver observer) {
        observers.add(observer);
        // Segera update observer baru dengan nilai saat ini
        observer.updateInventory(artifactKeys, foodCount);
    }

    // --- Data Manipulation Methods ---

    /** Dipanggil saat pemain mengambil makanan baru. */
    public void addFood(int amount) {
        foodCount += amount;

        System.out.println("Makanan bertambah. Total: " + foodCount);

        // Cek logika event (misal: hadiah setelah 5 makanan)
        if (foodCount > 0 && foodCount % 5 == 0) {
            System.out.println("--- TRIGGER EVENT: Pemain mendapatkan Artifact Key # " + (foodCount / 5));
            // Di sini Anda akan memanggil logika untuk memicu event hadiah,
            // misalnya melalui GameManager atau ItemFactory.
        }

        notifyObservers(); // PENTING: Beri tahu UI
    }

    /** Dipanggil saat makanan digunakan (misal: dimakan). */
    public void removeFood(int amount) {
        foodCount = Math.max(0, foodCount - amount);
        notifyObservers(); // PENTING: Beri tahu UI
    }

    /** Dipanggil saat pemain mendapatkan Artifact Key baru. */
    public void addArtifactKey(Item key) {
        artifactKeys.add(key);
        System.out.println("Kunci Baru Ditambahkan: " + key.getName()); // Asumsi Item punya getName()
        notifyObservers(); // PENTING: Beri tahu UI
    }

    // --- Notifikasi Observer (Pola Observer) ---

    private void notifyObservers() {
        // Memanggil updateInventory pada semua UI yang terdaftar
        for (InventoryObserver observer : observers) {
            observer.updateInventory(artifactKeys, foodCount);
        }
    }

    // --- Getters (Untuk keperluan logika game lain) ---
    public int getFoodCount() { return foodCount; }
    public List<Item> getArtifactKeys() { return artifactKeys; }
}
