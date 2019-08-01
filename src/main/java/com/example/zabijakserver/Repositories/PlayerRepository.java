package com.example.zabijakserver.Repositories;

import com.example.zabijakserver.Entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {


    List<Player> findByGame_Id(Long id);

    Player findByGame_IdAndPlayerId(Long id, Integer playerId);

    Player findOneByToken (Long token);

}
