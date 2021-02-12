package com.example.zabijakserver.integration;

import com.example.zabijakserver.Entities.*;
import com.example.zabijakserver.Exceptions.*;
import com.example.zabijakserver.PlayerService;
import com.example.zabijakserver.PlayerServiceImpl;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ComponentScan("com.example.zabijakserver")
public class PlayerServiceImplIntegrationTest {



//    @TestConfiguration
//    static class PlayerServiceImplTestContextConfiguration {
//
//        @MockBean
//        private PlayerRepository playerRepository;
//        @MockBean
//        private GameRepository gameRepository;
//        @MockBean
//        private KillLogRepository killLogRepository;
//
//
//        @Bean
//        public PlayerService playerService(){
//            return new PlayerServiceImpl(playerRepository, killLogRepository,
//                    gameRepository);
//        }
//    }



    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private KillLogRepository killLogRepository;


    private PlayerService playerService = new PlayerServiceImpl(playerRepository, killLogRepository, gameRepository);


    private Game game1;
    private Long game1Id = 1L;
    private Long game1Token;

    private Game game2;
    private Long game2Id = 2L;
    private Long game2Token;

    private Long randomGameId = 3L;
    private Long randomToken = 33333L;

    private Player david;
    private Player marta;
    private Player klara;
    private Player petr;
    
    private Long davidToken = 111L;
    private Long martaToken = 222L;
    private Long klaraToken = 333L;
    private Long petrToken = 444L;



    List<Game> games = new ArrayList<Game>();


    private List<Player> players = new ArrayList<>();
    private List<KillLog> killLogs;


    @Before
    public void setUp() {
        game1 = new Game();
        game1.setName("gameOne");
        game1.setToken(game1Id);
        game1.setId(game1Id);
        game1Token = game1.getToken();

        game2 = new Game();
        game2.setName("gameTwo");
        game2.setId(game2Id);
        game2.setToken(game2Id);
        game2Token = game2.getToken();


        games.add(game1);
        games.add(game2);

        david = new Player("david", 1, game1, false);
        david.setToken(davidToken);
        david.setGame(game1);

        marta = new Player("marta", 2, game1, false);
        marta.setToken(martaToken);
        marta.setGame(game1);

        klara = new Player("klara", 3, game1, false);
        klara.setToken(klaraToken);
        klara.setGame(game1);

        //PLAYER NOT IN GAME ONE
        petr = new Player("petr", 3, game1, false);
        petr.setToken(petrToken);


        players.add(david);
        players.add(marta);
        players.add(klara);

        game1.setPlayers(players);

        
        
        when(gameRepository.findOneByToken(game1.getToken())).thenReturn(game1);
        when(gameRepository.findOneById(game1Id)).thenReturn(game1);

/*        Mockito.when(gameRepository.save(game1)).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                game1.setId(1L);
                return null; }});*/

        when(gameRepository.findOneByToken(game2.getToken())).thenReturn(game2);
        when(gameRepository.findOneById(game2Id)).thenReturn(game2);

/*        Mockito.when(gameRepository.save(game2)).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
               game2.setId(2L);
               return null; }});*/
        
        when(gameRepository.findAll()).thenReturn(games);


        //PLAYER REPOSITORY
        when(playerRepository.findByGame_Id(game1.getId())).thenReturn(game1.getPlayers());
        
        when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), david.getPlayerId())).thenReturn(david);
        when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), marta.getPlayerId())).thenReturn(marta);
        when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), klara.getPlayerId())).thenReturn(klara);
        when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), petr.getPlayerId())).thenReturn(null);

        when(playerRepository.findOneByToken(david.getToken())).thenReturn(david);
        when(playerRepository.findOneByToken(marta.getToken())).thenReturn(marta);
        when(playerRepository.findOneByToken(klara.getToken())).thenReturn(klara);
        when(playerRepository.findOneByToken(petr.getToken())).thenReturn(petr);

    }

    //getGames()

    @Test
    public void getAllGamesGetAllGames(){
        List<Game> found = playerService.getGames();
        assertThat(found).isEqualTo(games);
    }

    //assignTargets()

    @Test
    public void afterAssignTargetsShouldTargetOfPlayerNotBeNull() throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        List<Player> players = playerService.startGame(game1Token);
        players.forEach(player -> assertThat(player.getTargetId()).isNotNull());
    }

    @Test
    public void gameSavedAndActive() throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        ArgumentCaptor<Game> argument = ArgumentCaptor.forClass(Game.class);
        when(gameRepository.save(argument.capture())).thenReturn(null);

        playerService.startGame(game1Token);
        Game foundGame = argument.getValue();

        assertThat(argument.getAllValues().size()).isEqualTo(1);
        assertThat(foundGame.getActive()).isTrue();
    }

    @Test
    public void assignTargetsShouldSavePlayers () throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        ArgumentCaptor<Player> argument = ArgumentCaptor.forClass(Player.class);
        when(playerRepository.save(argument.capture())).thenReturn(null);

        List<Player> playersFound = argument.getAllValues();
        playerService.startGame(game1.getToken());

        assertThat(playersFound.size()).isEqualTo(game1.getPlayers().size());
        playersFound.forEach(player -> assertThat(player.getTargetId()).isNotNull());
    }

    @Test
    public void assignTargetShouldAssignTargetsAsCircularReference () throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        List<Player> players = playerService.startGame(game1.getToken());
        List<Integer> playerIds = new ArrayList<>();
        List<Integer> playerTargets = new ArrayList<>();
        List<Integer> playerModified = new ArrayList<>();


        players.forEach(player -> {
            playerIds.add(player.getPlayerId());
            playerTargets.add(player.getTargetId());
        });

        playerModified.add(playerTargets.get(playerTargets.size()-1));
        playerModified.addAll(playerTargets);
        playerModified.remove(playerTargets.size()-1);


        assertThat(playerIds).isEqualTo(playerModified);

    }


    @Test
    public void assignTargetsOnNonExistentGameShouldThrowException(){
        assertThatThrownBy(() -> playerService.startGame(randomToken)).isInstanceOf(NotFoundException.class);
    }
    @Test
    public void startingGameWithNoPlayers(){
        assertThatThrownBy(() -> playerService.startGame(game2Token)).isInstanceOf(EmtpyGameException.class);
    }

    @Test
    public void startingActiveGame(){
        game1.setActive(Boolean.TRUE);
        assertThatThrownBy(() -> playerService.startGame(game1Token)).isInstanceOf(ModifyingActiveGameException.class);
    }


    // killPLayer()

    @Test
    public void killPlayerShouldUpdatePlayersTarget() throws GameIsNotActiveException, PlayerIsNotAliveException, InvalidPlayerException {
        //Setup
        when(playerRepository.save(any(Player.class))).thenAnswer(i -> i.getArguments()[0]);
        game1.setActive(Boolean.TRUE);
        david.setTargetId(marta.getPlayerId());
        marta.setTargetId(klara.getPlayerId());

        //Call
        Player player = playerService.killTarget(davidToken);

        //asertion
        assertEquals(player.getTargetId(), klara.getPlayerId());
    }

    @Test
    public void killPlayerShouldUpdateTargetAndSavePlayer() throws GameIsNotActiveException, PlayerIsNotAliveException, InvalidPlayerException {
        //Setup

        game1.setActive(Boolean.TRUE);
        david.setTargetId(marta.getPlayerId());
        marta.setTargetId(klara.getPlayerId());
        ArgumentCaptor<Player> argument = ArgumentCaptor.forClass(Player.class);


        when(playerRepository.save(argument.capture())).thenReturn(null);

        playerService.killTarget(davidToken);

        List<Player> playerAndTarget = argument.getAllValues();

        assertEquals(2, playerAndTarget.size());

        assertTrue(((playerAndTarget.get(1).getTargetId() == null && playerAndTarget.get(1).getAlive() == Boolean.FALSE)
                ^   (playerAndTarget.get(0).getTargetId() == null &&  playerAndTarget.get(0).getAlive() == Boolean.FALSE)));
    }

    @Test
    public void killPlayerUnknownToken() {
        assertThatThrownBy(() -> playerService.killTarget(randomToken)).isInstanceOf(InvalidPlayerException.class);
    }

    @Test
    public void killPlayerTargetNotValid() {
        david.setTargetId(petr.getTargetId());
        game1.setActive(Boolean.TRUE);

        assertThatThrownBy(() -> playerService.killTarget(davidToken)).isInstanceOf(InvalidPlayerException.class);
    }

    @Test
    public void killUsingAlivePlayer() {
        game1.setActive(Boolean.TRUE);
        david.setTargetId(marta.getPlayerId());
        david.setAlive(Boolean.FALSE);

        assertThatThrownBy(() -> playerService.killTarget(david.getToken())).isInstanceOf(PlayerIsNotAliveException.class);
    }

    @Test
    public void killInInactiveGame() {
        game1.setActive(Boolean.FALSE);
        david.setTargetId(marta.getPlayerId());
        david.setAlive(Boolean.TRUE);

        assertThatThrownBy(() -> playerService.killTarget(david.getToken())).isInstanceOf(GameIsNotActiveException.class);
    }

    @Test
    public void killPlayerSavesUpdatesGameWithKilllog() throws GameIsNotActiveException, PlayerIsNotAliveException, InvalidPlayerException {
        //Setup

        game1.setActive(Boolean.TRUE);
        david.setTargetId(marta.getPlayerId());
        marta.setTargetId(klara.getPlayerId());
        int killLogSize = game1.getKillLogs().size();

        ArgumentCaptor<Game> argument = ArgumentCaptor.forClass(Game.class);
        when(gameRepository.save(argument.capture())).thenReturn(null);

        //TEST
        playerService.killTarget(davidToken);

        //Number of method calls assertion
        assertThat(argument.getAllValues().size()).isEqualTo(1);

        int capturedKilllogsSize = argument.getAllValues().get(0).getKillLogs().size();

        //Assertion
        assertThat(capturedKilllogsSize).isEqualTo(killLogSize + 1);
    }

    @Test
    public void killPlayerCreatesKilllog() throws GameIsNotActiveException, PlayerIsNotAliveException, InvalidPlayerException {
        //Setup

        game1.setActive(Boolean.TRUE);
        david.setTargetId(marta.getPlayerId());
        marta.setTargetId(klara.getPlayerId());

        ArgumentCaptor<Game> argument = ArgumentCaptor.forClass(Game.class);
        when(gameRepository.save(argument.capture())).thenReturn(null);

        //TEST
        playerService.killTarget(davidToken);
        int killLogSize = argument.getValue().getKillLogs().size();
        KillLog killLogFound = argument.getValue().getKillLogs().get(killLogSize-1);

        assertTrue(killLogFound.getKillerId().equals(david.getPlayerId())
                       && killLogFound.getTargetId().equals(marta.getPlayerId()));

    }

    //Add player

    @Test
    public void PlayerReturnedShouldBeAlsoSaved() throws NotFoundException, ModifyingActiveGameException {
        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        when(playerRepository.save(argumentCaptor.capture())).thenReturn(null);

        Player returnPlayer = playerService.addPlayer(game1.getToken(), david.getName());

        assertThat(returnPlayer).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    public void AddingPlayerToActiveGameShouldThrowError() {
        game1.setActive(Boolean.TRUE);
        assertThatThrownBy(() -> playerService.addPlayer(game1.getToken(), david.getName())).isInstanceOf(ModifyingActiveGameException.class);

    }

    @Test
    public void AddingPlayerToNotActiveGameShouldThrowError() {
        assertThatThrownBy(() -> playerService.addPlayer(randomToken, david.getName())).isInstanceOf(NotFoundException.class);

    }

}
