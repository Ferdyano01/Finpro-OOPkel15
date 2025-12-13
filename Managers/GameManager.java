package com.Ferdyano.frontend.Managers;


/**
 * GameManager: Singleton Class yang mengelola state (keadaan) global permainan.
 * Bertanggung jawab atas Checkpoint, Game State (Exploration, Puzzle, Boss Fight),
 * dan bertindak sebagai Receiver untuk Game Commands.
 */
public class GameManager {

    // --- Singleton Implementation ---
    private static GameManager instance = new GameManager();

    // Konstruktor private mencegah instansiasi dari luar
    private GameManager() {
        // Inisialisasi state awal game
        this.currentGameState = GameState.EXPLORATION;
        this.currentCheckpoint = "START";
    }

    // Metode akses global untuk mendapatkan satu-satunya instansi
    public static GameManager getInstance() {
        return instance;
    }

    // --- Data Inti Game State ---

    // Enum untuk status permainan secara keseluruhan
    public enum GameState {
        EXPLORATION, SOLVING_PUZZLE, BOSS_FIGHT, GAME_OVER, PAUSED
    }

    private GameState currentGameState;
    private String currentCheckpoint;

    // Asumsi kita memiliki referensi ke pemain
    // private Player player;

    // --- Metode Manipulasi State Global ---

    /** Dipanggil oleh Command (MoveToCheckpointCommand) atau logika game. */
    public void setCurrentCheckpoint(String checkpointId) {
        System.out.println("Game State Updated: Pindah Checkpoint ke: " + checkpointId);
        this.currentCheckpoint = checkpointId;
        // Di sini bisa memanggil notifikasi untuk UI (jika ada mini-map)
    }

    /** Mengubah mode permainan, seringkali dipanggil saat interaksi atau transisi. */
    public void setGameState(GameState newState) {
        if (this.currentGameState != newState) {
            System.out.println("Game State Transition: " + this.currentGameState + " -> " + newState);
            this.currentGameState = newState;

            // Logika spesifik transisi:
            if (newState == GameState.SOLVING_PUZZLE) {
                // Hentikan movement pemain, tampilkan PuzzleInterface
                // Lock player input
            } else if (newState == GameState.EXPLORATION) {
                // Aktifkan kembali movement pemain
            }
        }
    }

    // --- Getters ---

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public String getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    // ... Metode untuk menyimpan/memuat game, dll.
}
