package com.Ferdyano.frontend.ui;

import com.Ferdyano.frontend.pattern.GameCommand;
import com.Ferdyano.frontend.command.AlignCelestialInstrumentsCommand; // Command spesifik
import com.Ferdyano.frontend.core.CelestialInstrument; // Objek yang dimanipulasi
import com.Ferdyano.frontend.Managers.GameManager; // Untuk mengubah Game State
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import java.util.Stack;

/**
 * PuzzleInterface: Overlay interaktif untuk memecahkan teka-teki.
 * Bertindak sebagai Invoker dalam Pola Command.
 */
public class PuzzleInterface extends Table {

    // Tumpukan untuk menyimpan Command yang sudah dieksekusi (untuk UNDO)
    private final Stack<GameCommand> commandHistory = new Stack<>();
    // Tumpukan untuk menyimpan Command yang di-undo (untuk REDO)
    private final Stack<GameCommand> commandRedoStack = new Stack<>();

    // Objek puzzle (Receiver) yang akan dimanipulasi
    private final CelestialInstrument instrument1;

    public PuzzleInterface(Skin skin, CelestialInstrument instrument1) {
        // Asumsi PuzzleInterface dibuat saat pemain mencapai teka-teki
        this.instrument1 = instrument1;

        // --- Setup UI ---
        // Atur agar interface mengisi layar
        setFillParent(true);
        setBackground(skin.getDrawable("puzzle_background"));

        // --- Setup Tombol Rotasi ---
        TextButton rotateButton = new TextButton("Rotate +15°", skin);
        rotateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Langkah 1: Buat Command baru
                GameCommand command = new AlignCelestialInstrumentsCommand(instrument1, 15);

                // Langkah 2: Eksekusi dan Simpan
                executeCommand(command);
            }
        });

        // --- Setup Tombol Undo ---
        TextButton undoButton = new TextButton("UNDO", skin);
        undoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                undoLastCommand();
            }
        });

        // --- Layout Scene2D ---
        add(new Label("Celestial Alignment", skin, "title")).colspan(2).row();
        // [Tambahkan visualisasi instrumen di sini]
        add(rotateButton).pad(10);
        add(undoButton).pad(10).row();

        // --- Ubah Game State ---
        // Saat interface ini dibuat, beri tahu GameManager
        GameManager.getInstance().setGameState(GameManager.GameState.SOLVING_PUZZLE);
    }

    /** * Memanggil Command untuk dieksekusi dan menyimpannya ke histori.
     */
    private void executeCommand(GameCommand command) {
        command.execute(); // Memicu aksi (misal: memutar instrumen)
        commandHistory.push(command); // Simpan untuk Undo
        commandRedoStack.clear(); // Hapus stack Redo jika ada aksi baru
        checkPuzzleCompletion();
    }

    /**
     * Membatalkan Command terakhir yang dieksekusi.
     */
    private void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            GameCommand command = commandHistory.pop();
            command.undo(); // Membatalkan aksi
            commandRedoStack.push(command); // Simpan ke stack Redo
            checkPuzzleCompletion();
        } else {
            System.out.println("History kosong. Tidak ada yang bisa di-undo.");
        }
    }

    private void checkPuzzleCompletion() {
        // Logika untuk memeriksa apakah Instrument 1, 2, dan 3 sudah sejajar
        // If (puzzleSolved) {
        //    GameManager.getInstance().setGameState(GameManager.GameState.EXPLORATION);
        //    // Hapus interface ini dari stage
        // }
    }

    // Metode untuk keluar (jika pemain menyerah)
    public void exitPuzzle() {
        GameManager.getInstance().setGameState(GameManager.GameState.EXPLORATION);
    }
}
