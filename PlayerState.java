package com.Ferdyano.frontend.pattern;

import com.Ferdyano.frontend.core.Player; // Perlu import class Player

/**
 * PlayerState: Kontrak untuk status perilaku pemain (misalnya, kecepatan gerakan, kemampuan berinteraksi).
 * Diimplementasikan oleh HealthyState, HungryState, dll.
 */
public interface PlayerState {

    /** * Dipanggil saat pemain memasuki state ini (misal: membatasi gerakan).
     */
    void enterState(Player player);

    /** * Menangani input spesifik (misal: mencegah lari saat lapar).
     */
    void handleInput(Player player);

    /** * Logika update yang unik untuk state ini (misal: HP berkurang perlahan saat critical).
     */
    void update(Player player, float delta);
}
