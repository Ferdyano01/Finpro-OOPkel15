package com.Ferdyano.frontend.Managers;

import com.Ferdyano.frontend.pattern.HealthObserver; // Asumsi package architecture Anda
import com.Ferdyano.frontend.pattern.SurvivalObserver; // Asumsi package architecture Anda
import java.util.ArrayList;
import java.util.List;

/**
 * HealthManager: Singleton Class yang mengelola data HP, Lapar, dan Haus.
 * Bertindak sebagai Subject (Pemberi Notifikasi) dalam Pola Observer.
 */
public class HealthManager {

    // --- Singleton Implementation ---
    private static HealthManager instance = new HealthManager();

    // Konstruktor private mencegah instansiasi dari luar
    private HealthManager() {}

    // Metode akses global untuk mendapatkan satu-satunya instansi
    public static HealthManager getInstance() {
        return instance;
    }

    // --- Data Inti Game State ---

    private int currentHp = 100;
    private final int maxHp = 100;
    private int hungerLevel = 100; // 100 = kenyang (Level ini akan berkurang seiring waktu)
    private int thirstLevel = 100; // 100 = tidak haus (Level ini akan berkurang seiring waktu)

    // --- Observer Lists (Untuk Notifikasi UI) ---

    private final List<HealthObserver> healthObservers = new ArrayList<>();
    private final List<SurvivalObserver> survivalObservers = new ArrayList<>();

    // --- Observer Registration Methods ---

    /** Mendaftarkan komponen UI (misalnya SurvivalHud) untuk menerima update HP. */
    public void registerHealthObserver(HealthObserver observer) {
        healthObservers.add(observer);
        // Segera update observer baru dengan nilai saat ini
        observer.updateHealth(currentHp, maxHp);
    }

    /** Mendaftarkan komponen UI untuk menerima update Lapar/Haus. */
    public void registerSurvivalObserver(SurvivalObserver observer) {
        survivalObservers.add(observer);
        // Segera update observer baru dengan nilai saat ini
        observer.updateSurvivalMeters(hungerLevel, thirstLevel);
    }

    // --- Data Manipulation Methods (Dipanggil oleh Game Logic) ---

    /** Mengubah HP pemain (positif untuk penyembuhan, negatif untuk damage). */
    public void changeHealth(int delta) {
        currentHp += delta;
        currentHp = Math.max(0, Math.min(maxHp, currentHp)); // Batasi nilai antara 0 dan 100
        notifyHealthObservers(); // PENTING: Beri tahu UI
    }

    /** Mengisi kembali meter Lapar. */
    public void consumeFood(int restoreValue) {
        hungerLevel += restoreValue;
        hungerLevel = Math.min(100, hungerLevel);
        notifySurvivalObservers(); // PENTING: Beri tahu UI
    }

    /** Mengisi kembali meter Haus. */
    public void consumeWater(int restoreValue) {
        thirstLevel += restoreValue;
        thirstLevel = Math.min(100, thirstLevel);
        notifySurvivalObservers(); // PENTING: Beri tahu UI
    }

    // --- Notifikasi Observer (Pola Observer) ---

    /** Iterasi melalui semua komponen yang tertarik dan memanggil update. */
    private void notifyHealthObservers() {
        for (HealthObserver observer : healthObservers) {
            observer.updateHealth(currentHp, maxHp);
        }
    }

    private void notifySurvivalObservers() {
        for (SurvivalObserver observer : survivalObservers) {
            observer.updateSurvivalMeters(hungerLevel, thirstLevel);
        }
    }

    // --- Game Loop Update ---

    /** Dipanggil di render loop LibGDX untuk pengurangan HP/Survival seiring waktu. */
    public void update(float delta) {
        // Implementasi logika pengurangan survival di sini
        // Contoh: Mengurangi level setiap 5 detik
        // timeSinceLastTick += delta;
        // if (timeSinceLastTick > 5.0f) {
        //     hungerLevel--;
        //     thirstLevel--;
        //     // ... cek jika < 0 dan kurangi HP
        //     notifySurvivalObservers();
        //     notifyHealthObservers();
        // }
    }
}
