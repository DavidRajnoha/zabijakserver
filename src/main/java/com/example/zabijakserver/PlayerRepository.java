package com.example.zabijakserver;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByName(String name);

    List<Player> findByPlayerId(Long playerId);

    List<Player> findByGame_Id(Long id);
}
