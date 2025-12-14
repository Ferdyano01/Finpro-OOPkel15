package com.lostkey.backend.repository;

import com.lostkey.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Player> findTop10ByOrderByHighScoreDesc();

    long countByIsBossDefeatedTrue();
}