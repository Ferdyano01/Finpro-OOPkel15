package com.Ferdyano.frontend.command;

import com.Ferdyano.frontend.pattern.GameCommand;
import com.Ferdyano.frontend.Managers.GameManager; // Asumsi ada GameManager untuk checkpoint

/**
 * Perintah untuk memindahkan pemain ke Checkpoint tertentu.
 */
public class MoveToCheckpointCommand implements GameCommand {

    private final String checkpointId;
    private final String previousCheckpointId;
    private final GameManager gameManager; // Receiver/Penerima aksi

    public MoveToCheckpointCommand(String checkpointId, String previousCheckpointId) {
        this.checkpointId = checkpointId;
        this.previousCheckpointId = previousCheckpointId;
        // Asumsi GameManager adalah Singleton dan bisa diakses
        this.gameManager = GameManager.getInstance();
    }

    @Override
    public void execute() {
        // Logika: Pemain bergerak ke Checkpoint baru
        System.out.println("Executing: Pindah ke Checkpoint " + checkpointId);
        gameManager.setCurrentCheckpoint(checkpointId);
        // Di sini akan ada logika visual pergerakan (animasi, transisi)
    }

    @Override
    public void undo() {
        // Logika: Memindahkan pemain kembali ke Checkpoint sebelumnya
        System.out.println("Undoing: Kembali ke Checkpoint " + previousCheckpointId);
        gameManager.setCurrentCheckpoint(previousCheckpointId);
        // Hanya dapat dilakukan jika game mengizinkan undo (misal: di mode tertentu)
    }
}
