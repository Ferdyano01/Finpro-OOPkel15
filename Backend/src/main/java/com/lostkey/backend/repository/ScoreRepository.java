package com.lostkey.backend.repository;

import com.lostkey.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    // Custom query to find top scores for the leaderboard
    List<Score> findTop10ByOrderByHighScoreDesc();
}