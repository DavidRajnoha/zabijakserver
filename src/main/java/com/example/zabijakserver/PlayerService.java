package com.example.zabijakserver;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.KillLog;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Exceptions.*;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private KillLogRepository killLogRepository;

    @Autowired
    private GameRepository gameRepository;

    private static final Logger log = LoggerFactory.getLogger(ZabijakserverApplication.class);


    public Player killTarget(Long token) throws PlayerIsNotAliveException, GameIsNotActiveException {

        log.info("Kill target was invoked");
        Player player;
        Player target;
        Game game;
        try {
            player = playerRepository.findOneByToken(token);
            if (!player.getAlive()) {
                throw new PlayerIsNotAliveException("");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidPlayerException("Token is invalid");
        }

        game = player.getGame();
        if (!game.getActive()) throw new GameIsNotActiveException("");

        try {
            target = playerRepository.findByGame_IdAndPlayerId(game.getId(), player.getTargetId());
        } catch (IndexOutOfBoundsException e){
            throw new InvalidPlayerException(player + " can not find his target in game with id: " + game.getId());
        }

        player.setTargetId(target.getTargetId());
        target.setTargetId(null);
        target.setAlive(false);
        KillLog killLog = new KillLog(game, player.getPlayerId(), target.getPlayerId());


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

    public Player addPlayer(Long gameToken, String name) throws ModifyingActiveGameException, NotFoundException {
        Game game = gameRepository.findOneByToken(gameToken);
        if (game == null) {throw new NotFoundException("");}

        if (game.getActive()){
            throw new ModifyingActiveGameException("Cannot add player to the active game");
        }

        Player player = new Player(name, playerRepository.findByGame_Id(game.getId()).size() + 1, gameRepository.findOneByToken(gameToken));
        playerRepository.save(player);
        return player;

    }

    public Game createGame(String name){
        Game newGame = new Game(name);
        gameRepository.save(newGame);
        newGame.setToken();
        gameRepository.save(newGame);
        return newGame;
    }

    public List<Player> assignTargets(Long gameToken) throws ModifyingActiveGameException, NotFoundException, EmtpyGameException {
        Game game = gameRepository.findOneByToken(gameToken);
        if (game == null) throw new NotFoundException("");
        if (game.getActive()) throw new ModifyingActiveGameException("");

        List<Player> players = playerRepository.findByGame_Id(game.getId());
        if (players.size() < 3) throw new EmtpyGameException("");

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


    public List<KillLog> getKillLog(Long gameId) throws NotFoundException {
        List<KillLog> killLogs = killLogRepository.findByGame_Id(gameId);
        if (killLogs.isEmpty()) throw new NotFoundException("");
        return killLogs;
    }

}
