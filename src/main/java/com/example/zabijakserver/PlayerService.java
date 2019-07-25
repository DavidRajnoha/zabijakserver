package com.example.zabijakserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private KillLogRepository killLogRepository;

    @Autowired
    private GameRepository gameRepository;

    private static final Logger log = LoggerFactory.getLogger(ZabijakserverApplication.class);


    //TODO: Implement killLog
    public void killTarget(Long playerId){
        playerRepository.findByPlayerId(playerId).forEach(player -> {
            playerRepository.findByPlayerId(player.getTargetId()).forEach(target ->{
                player.setTargetId(target.getTargetId());
                target.setTargetId(null);
                target.setAlive(false);
                log.info(player.toString() + "has killed" + target.toString());
                killLogRepository.save(new KillLog(player.getGame(), playerId, target.getPlayerId()));

            });
            log.info("First lambda was invoked");
        });

        log.info("Kill target was invoked");

    }

    public void addPlayer(String name, Long playerId, Long targetId){
        playerRepository.save(new Player(name, playerId, targetId ));
        log.info("Player has been saved");
    }

    public void createGame(String name, Player... players){
        gameRepository.save(new Game(name, players));
    }


    public void createGame(String name, String... playerNames){
        Set<Player> players = new HashSet<>();
        Set<String> playerNamesSet = Stream.of(playerNames).collect(Collectors.toSet());
        AtomicLong playerId = new AtomicLong(0);

        playerNamesSet.forEach(playerName -> {
            players.add(new Player(playerName, playerId.incrementAndGet(), null));
        });

        gameRepository.save(new Game(name, players));
    }

}
