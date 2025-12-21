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
        observer.updateHealth(currentHp,