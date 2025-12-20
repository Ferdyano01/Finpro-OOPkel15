package com.lostkey.backend.service;

import com.lostkey.backend.model.Player;
import com.lostkey.backend.model.Score;
import com.lostkey.backend.repository.PlayerRepository;
import com.lostkey.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ScoreService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    public Score updateHighScore(UUID playerId, int newScore) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Score score = player.getScore();
        score.updateScoreIfHigher(newScore);
        
        return scoreRepository.save(score);
    }

    public List<Score> getLeaderboard() {
        return scoreRepository.findTop10ByOrderByHighScoreDesc();
    }
}