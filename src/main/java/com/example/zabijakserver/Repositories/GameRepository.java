package com.example.zabijakserver.Repositories;

import com.example.zabijakserver.Entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findOneById(Long id);
}
