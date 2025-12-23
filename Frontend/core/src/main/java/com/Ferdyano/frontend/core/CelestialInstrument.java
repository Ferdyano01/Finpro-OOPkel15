package com.Ferdyano.frontend.core;

public class CelestialInstrument {
    private final String name;
    private int currentAngle; // Sudut saat ini (State yang diubah Command)

    public CelestialInstrument(String name, int initialAngle) {
        this.name = name;
        this.currentAngle = initialAngle;
    }

    /** * Metode utama yang dipanggil oleh Command untuk merotasi objek.
     */
    public void rotate(int angleChange) {
        System.out.println(name + " diputar sebesar " + angleChange + " derajat.");
        this.currentAngle = (this.currentAngle + angleChange) % 360; // Pastikan sudut 0-359
        // Di sini akan ada logika untuk memicu update visual
    }

    /** * Metode yang dipanggil oleh Command.undo() untuk mengembalikan sudut.
     */
    public void setAngle(int angle) {
        System.out.println(name + " sudut diatur kembali ke " + angle + " derajat.");
        this.currentAngle = angle;
    }

    // Getter yang dibutuhkan oleh Command untuk menyimpan state sebelumnya
    public int getCurrentAngle() {
        return currentAngle;
    }

    public String getName() {
        return name;
    }
}

