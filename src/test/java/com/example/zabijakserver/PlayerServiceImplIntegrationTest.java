package com.example.zabijakserver;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.KillLog;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Exceptions.EmtpyGameException;
import com.example.zabijakserver.Exceptions.ModifyingActiveGameException;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@RunWith(SpringRunner.class)
@ComponentScan("com.example.zabijakserver")
public class PlayerServiceImplIntegrationTest {

    @TestConfiguration
    static class PlayerServiceImplTestContextConfiguration {
        @Bean
        public PlayerService playerService(){
            return new PlayerServiceImpl();
        }
    }

    @Autowired
    private PlayerService playerService;

    @MockBean
    private PlayerRepository playerRepository;
    @MockBean
    private GameRepository gameRepository;
    @MockBean
    private KillLogRepository killLogRepository;

    private Game game1 = new Game();
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
    
    private Long davidToken;
    private Long martaToken;
    private Long klaraToken;
    private Long petrToken;



    List<Game> games = new ArrayList<Game>();


    private List<Player> players = new ArrayList<>();
    private List<KillLog> killLogs;


    @Before
    public void setUp() {
        game1 = new Game("gameOne");
        game1.setToken(game1Id);
        game1.setId(game1Id);
        game1Token = game1.getToken();

        game2 = new Game("gameTwo");
        game2.setId(game2Id);
        game2.setToken();
        game2Token = game2.getToken();


        games.add(game1);
        games.add(game2);

        david = new Player("david", 1, game1);
        davidToken = david.getToken();
        
        marta = new Player("marta", 2, game1);
        martaToken = marta.getToken();
        
        klara = new Player("klara", 3, game1);
        klaraToken = klara.getToken();

        petr = new Player("petr", 3, game1);
        petrToken = petr.getToken();


        players.add(david);
        players.add(marta);
        players.add(klara);
        players.add(petr);
        
        
        Mockito.when(gameRepository.findOneByToken(game1.getToken())).thenReturn(game1);
        Mockito.when(gameRepository.findOneById(game1Id)).thenReturn(game1);

/*        Mockito.when(gameRepository.save(game1)).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                game1.setId(1L);
                return null; }});*/

        Mockito.when(gameRepository.findOneByToken(game2.getToken())).thenReturn(game2);
        Mockito.when(gameRepository.findOneById(game2Id)).thenReturn(game2);

/*        Mockito.when(gameRepository.save(game2)).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
               game2.setId(2L);
               return null; }});*/
        
        Mockito.when(gameRepository.findAll()).thenReturn(games);


        //PLAYER REPOSITORY
        Mockito.when(playerRepository.findByGame_Id(game1.getId())).thenReturn(players);
        
        Mockito.when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), david.getPlayerId())).thenReturn(david);
        Mockito.when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), marta.getPlayerId())).thenReturn(marta);
        Mockito.when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), klara.getPlayerId())).thenReturn(klara);
        Mockito.when(playerRepository.findByGame_IdAndPlayerId(game1.getId(), petr.getPlayerId())).thenReturn(petr);
            }


    @Test
    public void getAllGamesGetAllGames(){
        List<Game> found = playerService.getGames();
        assertThat(found.equals(games));
    }

    @Test
    public void afterAssignTargetsShouldTargetOfPlayerNotBeNull() throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        List<Player> players = playerService.assignTargets(game1Token);
        Player player = players.get(1);
        assertThat(player.getTargetId() != 0);
    }

    @Test
    public void assignTargetsOnNonExistentGameShouldThrowException() throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        assertThatThrownBy(() -> playerService.assignTargets(randomToken)).isInstanceOf(NotFoundException.class);
    }
    @Test
    public void startingGameWithNoPlayers() throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        assertThatThrownBy(() -> playerService.assignTargets(game2Token)).isInstanceOf(EmtpyGameException.class);
    }

    @Test
    public void startingActiveGame() throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        game1.setActive(Boolean.TRUE);
        assertThatThrownBy(() -> playerService.assignTargets(game1Token)).isInstanceOf(ModifyingActiveGameException.class);
    }

}
