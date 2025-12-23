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

    private static final HealthManager instance = new HealthManager();

    private HealthManager() {}

    public static HealthManager getInstance() {
        return instance;
    }

    private int currentHp = 100;
    private final int maxHp = 100;
    private int hungerLevel = 100;
    private int thirstLevel = 100;
    private float timer = 0;

    // Kecepatan berkurang: 1.5 detik agar perubahan di bar terlihat jelas
    private static final float DECAY_TIME = 1.5f;

    private final List<HealthObserver> healthObservers = new ArrayList<>();
    private final List<SurvivalObserver> survivalObservers = new ArrayList<>();

    public void setValues(int hp, int hunger, int thirst) {
        this.currentHp = hp;
        this.hungerLevel = hunger;
        this.thirstLevel = thirst;
        notifyHealthObservers();
        notifySurvivalObservers();
    }

    public void registerHealthObserver(HealthObserver observer) {
        if (!healthObservers.contains(observer)) {
            healthObservers.add(observer);
        }
        observer.updateHealth(currentHp, maxHp);
    }

    public void registerSurvivalObserver(SurvivalObserver observer) {
        if (!survivalObservers.contains(observer)) {
            survivalObservers.add(observer);
        }
        observer.updateSurvivalMeters(hungerLevel, thirstLevel);
    }

    public void clearObservers() {
        healthObservers.clear();
        survivalObservers.clear();
        System.out.println("HealthManager: Observers cleared.");
    }

    public void changeHealth(int delta) {
        currentHp += delta;
        currentHp = Math.max(0, Math.min(maxHp, currentHp));
        notifyHealthObservers();
    }

    // Mengambil Buah menambah Lapar dan HP
    public void consumeFood(int restoreValue) {
        hungerLevel = Math.min(100, hungerLevel + restoreValue);
        changeHealth(15); // Ditambah agar HP terasa naik
        notifySurvivalObservers();
    }

    // Mengambil Air menambah Haus dan HP
    public void consumeWater(int restoreValue) {
        thirstLevel = Math.min(100, thirstLevel + restoreValue);
        changeHealth(10); // Ditambah agar HP terasa naik
        notifySurvivalObservers();
    }

    private void notifyHealthObservers() {
        for (HealthObserver observer : new ArrayList<>(healthObservers)) {
            observer.updateHealth(currentHp, maxHp);
        }
    }

    private void notifySurvivalObservers() {
        for (SurvivalObserver observer : new ArrayList<>(survivalObservers)) {
            observer.updateSurvivalMeters(hungerLevel, thirstLevel);
        }
    }

    // Method ini harus dipanggil di render() Screen
    public void update(float delta) {
        timer += delta;
        if (timer >= DECAY_TIME) {
            // Berkurang otomatis agar player butuh mencari item
            hungerLevel = Math.max(0, hungerLevel - 2);
            thirstLevel = Math.max(0, thirstLevel - 3);

            // Jika Lapar atau Haus habis, HP berkurang drastis
            if (hungerLevel <= 0 || thirstLevel <= 0) {
                changeHealth(-5);
            }

            notifySurvivalObservers();
            timer = 0;
        }
    }

    public void resetStatus() {
        this.currentHp = 100;
        this.hungerLevel = 100;
        this.thirstLevel = 100;
        this.timer = 0;
        notifyHealthObservers();
        notifySurvivalObservers();
    }

    public int getHunger() { return hungerLevel; }
    public int getThirst() { return thirstLevel; }
    public int getHealth() { return currentHp; }
}
