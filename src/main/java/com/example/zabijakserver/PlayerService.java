package com.example.zabijakserver;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.KillLog;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Exceptions.InvalidPlayerException;
import com.example.zabijakserver.Exceptions.ModifyingActiveGameException;
import com.example.zabijakserver.Exceptions.PlayerIsNotAliveException;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import javassist.NotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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


    public Player killTarget(Long gameId, Integer playerId) throws PlayerIsNotAliveException {

        log.info("Kill target was invoked");
        Player player;
        Player target;
        Game game;
        try {
            player = playerRepository.findByGame_IdAndPlayerId(gameId, playerId);
            if (!player.getAlive()) {
                throw new PlayerIsNotAliveException("");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidPlayerException("Player with index: " + playerId + " does not exist in game with id: " + gameId);
        }
        try {
            target = playerRepository.findByGame_IdAndPlayerId(gameId, player.getTargetId());
        } catch (IndexOutOfBoundsException e){
            throw new InvalidPlayerException(player + " can not find his target in game with id: " + gameId);
        }

        game = player.getGame();
        player.setTargetId(target.getTargetId());
        target.setTargetId(null);
        target.setAlive(false);
        KillLog killLog = new KillLog(game, playerId, target.getPlayerId());


        log.info(player.toString() + " has killed " + target.toString());
        game.addKillLog(killLog);


        if (player.getTargetId() == null) {
            game.setActive(Boolean.FALSE);
            //TODO: Set game as winned
        }

        gameRepository.save(game);
        playerRepository.save(player);
        playerRepository.save(target);

        return player;
    }

    public Player addPlayer(Long gameId, String name) throws ModifyingActiveGameException, NotFoundException {
        Game game = gameRepository.findOneById(gameId);
        if (game == null) {throw new NotFoundException("");}

        if (game.getActive()){
            throw new ModifyingActiveGameException("Cannot add player to the active game");
        }

        Player player = new Player(name, playerRepository.findByGame_Id(gameId).size() + 1);
        player.setGame(gameRepository.findOneById(gameId));
        playerRepository.save(player);
        return player;

    }



//    public void createGame(String name, Player... players){
//        gameRepository.save(new Game(name, players));
//    }




   public void createGame(String name, String... playerNames){
        List<String> playerNamesSet = Stream.of(playerNames).collect(Collectors.toList());
        createGame(name, playerNamesSet);
    }

    public Game createGame(String name, List<String> playerNamesSet){
        List<Player> players = new ArrayList<>();
        AtomicInteger playerId = new AtomicInteger(0);

        playerNamesSet.forEach(playerName -> players.add(new Player(playerName, playerId.incrementAndGet())));

        Game newGame = new Game(name, players);
        gameRepository.save(newGame);
        return newGame;
    }

    public List<Player> assignTargets(Long gameId) throws ModifyingActiveGameException, NotFoundException {
        List<Player> players = playerRepository.findByGame_Id(gameId);
        Game game = gameRepository.findOneById(gameId);
        if (game == null) throw new NotFoundException("");
        if (game.getActive()) throw new ModifyingActiveGameException("");

        Collections.shuffle(players);

        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            players.get(i).setTargetId(players.get((i+1)%playersSize).getPlayerId());
            playerRepository.save(players.get(i));
        }
        game.setActive(Boolean.TRUE);
        gameRepository.save(game);
        return players;
    }

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public List<Player> getPlayers(Long gameId) throws NotFoundException {
        List<Player> players = playerRepository.findByGame_Id(gameId);
        if (players.isEmpty()) throw new NotFoundException("");
        return players;
    }

    public Game getGame(Long gameId) throws NotFoundException {
        Game game = gameRepository.findOneById(gameId);
        if (game==null) throw new NotFoundException("");
        return game;
    }

    public List<KillLog> getKilllogs() {
        return killLogRepository.findAll();
    }

    public List<KillLog> getKillLog(Long gameId) throws NotFoundException {
        List<KillLog> killLogs = killLogRepository.findByGame_Id(gameId);
        if (killLogs.isEmpty()) throw new NotFoundException("");
        return killLogs;
    }
}
