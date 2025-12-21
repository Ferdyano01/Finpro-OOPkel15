package com.Ferdyano.frontend.command;

import com.Ferdyano.frontend.pattern.GameCommand; // Import GameCommand
import com.Ferdyano.frontend.core.CelestialInstrument; // Import Receiver

/**
 * Perintah untuk menyesuaikan (Align) salah satu instrumen dalam puzzle.
 */
public class AlignCelestialInstrumentsCommand implements GameCommand {

    private final CelestialInstrument instrument; // Receiver
    private final int angleChange;
    private final int previousAngle; // State yang disimpan untuk Undo

    public AlignCelestialInstrumentsCommand(CelestialInstrument instrument, int angleChange) {
        this.instrument = instrument;
        this.angleChange = angleChange;
        // Menyimpan state objek SEBELUM Command dieksekusi
        this.previousAngle = instrument.getCurrentAngle();
    }

    @Override
    public void execute() {
        // Melaksanakan perintah pada Receiver
        instrument.rotate(angleChange);
        // Logika tambahan: Periksa apakah puzzle selesai
    }

    @Override
    public void undo() {
        // Mengembalikan state Receiver ke keadaan sebelum execute() dipanggil
        instrument.setAngle(previousAngle);
    }
}
