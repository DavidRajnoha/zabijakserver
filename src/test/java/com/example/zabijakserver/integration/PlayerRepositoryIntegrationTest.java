package com.example.zabijakserver.integration;


import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.KillLog;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("com.example.zabijakserver")
public class PlayerRepositoryIntegrationTest {



    @Test
    public void contextLoads() throws Exception{
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private KillLogRepository killLogRepository;

    private Game game;
    private List<Player> players;
    private List<KillLog> killLogs;
    private Long gameId;
    private Long gameToken;
    private Long playerToken;


    @Before
    public void setUp() {
        Integer playerId = 1;
        game = new Game();
        players = new ArrayList<>();
        killLogs = new ArrayList<>();
        gameRepository.save(game);

        gameId = game.getId();
        gameToken = game.getToken();

        players.add(new Player("David", 1, game));
        players.add(new Player("Tom", 2, game));
        players.add(new Player("Marta", 3, game));
        players.add(new Player("Petr", 4, game));

        killLogs.add(new KillLog(game, 1, 2));
        killLogs.add(new KillLog(game, 1, 3));
        killLogs.add(new KillLog(game, 1, 4));
        killLogs.add(new KillLog(game, 1, 1));
    }


    @Test
    public void findSavedGameById() {
        Game foundGame = gameRepository.findOneById(gameId);
        assertThat(game).isEqualTo(foundGame);
    }

    @Test
    public void findSavedGameByToken() {
        Game foundGame = gameRepository.findOneByToken(gameToken);
        assertThat(game).isEqualTo(foundGame);
    }


    @Test
    public void playersFoundByGameIdAreSameAsPlayersAttributeOfThatGame() {
        //given
        players.forEach(player -> playerRepository.save(player));
        //when
        List<Player> playersFromPlayerRepository = playerRepository.findByGame_Id(game.getId());
        List<Player> playersFromGame = game.getPlayers();
        //then
        assertThat(playersFromPlayerRepository).isEqualTo(playersFromGame);
    }

    @Test
    public void killLogsFoundByGameIdAreSameAsKillLogsAttributeOfThatGame() {
        //given
        killLogs.forEach(killLog -> killLogRepository.save(killLog));
        //when
        List<KillLog> foundKillLogRepository = killLogRepository.findByGame_Id(game.getId());
        List<KillLog> foundGame = game.getKillLogs();
        //then
        assertThat(foundKillLogRepository).isEqualTo(foundGame);
    }




}
