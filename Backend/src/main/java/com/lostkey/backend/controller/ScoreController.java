package com.lostkey.backend.controller;

import com.lostkey.backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updateScore(@PathVariable UUID playerId, @RequestBody ScoreRequest request) {
        return ResponseEntity.ok(scoreService.updateHighScore(playerId, request.score));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        return ResponseEntity.ok(scoreService.getLeaderboard());
    }

    public static class ScoreRequest {
        public int score;
    }
}