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
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

    private KillLogRepository killLogRepository;

    private GameRepository gameRepository;

    private static final Logger log = LoggerFactory.getLogger(ZabijakserverApplication.class);

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, KillLogRepository killLogRepository,
                             GameRepository gameRepository){
        this.playerRepository = playerRepository;
        this.killLogRepository = killLogRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * TESTING: COVERED
     * */
    public Player killTarget(Long token) throws PlayerIsNotAliveException, GameIsNotActiveException,
            InvalidPlayerException {


        Player player = playerRepository.findOneByToken(token);
        if (player == null) throw new InvalidPlayerException("Could not find player by the token you provided");

        Game game = player.getGame();
        if (!game.getActive()) throw new GameIsNotActiveException("The game has not been started yet");

        Player target = playerRepository.findByGame_IdAndPlayerId(game.getId(), player.getTargetId());
        if (target == null) throw new InvalidPlayerException("You don't have any target anymore. Take a rest");

        if (!player.getAlive()) { throw new PlayerIsNotAliveException("You can't kill with not alive player "); }

        player.setTargetId(target.getTargetId());
        target.setTargetId(null);
        target.setAlive(false);
        KillLog killLog = new KillLog(game, player.getPlayerId(), target.getPlayerId());

        game.addKillLog(killLog);


        if (player.getTargetId() == null) {
            game.setActive(Boolean.FALSE);
            player.setWinner(true);
        }

        gameRepository.save(game);
        playerRepository.save(target);
        playerRepository.save(player);

        return player;
    }

    public Player addPlayer(Long gameToken, String name) throws ModifyingActiveGameException, NotFoundException {
        Game game = gameRepository.findOneByToken(gameToken);
        if (game == null) {throw new NotFoundException("");}

        if (game.getActive()){throw new ModifyingActiveGameException("Cannot add player to the active game"); }

        Player player = new Player(name, game.getPlayers().size() + 1, game);
        playerRepository.save(player);
        return player;

    }


    public Game createGame(String name){
        Game newGame = new Game();
        newGame.setName(name);

        gameRepository.save(newGame);
        newGame.setToken();
        gameRepository.save(newGame);
        return newGame;
    }


    public List<Player> startGame(Long gameToken) throws ModifyingActiveGameException, NotFoundException, EmtpyGameException {
        Game game = gameRepository.findOneByToken(gameToken);
        if (game == null) throw new NotFoundException("");
        if (game.getActive()) throw new ModifyingActiveGameException("");

        List<Player> players = playerRepository.findByGame_Id(game.getId());
        if (players.size() < 3) throw new EmtpyGameException("");

        assignTargets(players);

        game.setActive(Boolean.TRUE);
        gameRepository.save(game);
        return players;
    }

    private void assignTargets(List<Player> players){
        Collections.shuffle(players);

        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            players.get(i).setTargetId(players.get((i+1)%playersSize).getPlayerId());
            playerRepository.save(players.get(i));
        }
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
