package com.example.zabijakserver.unit;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Exceptions.EmtpyGameException;
import com.example.zabijakserver.Exceptions.ModifyingActiveGameException;
import com.example.zabijakserver.PlayerServiceImpl;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;


public class PlayerServiceTests {
    private PlayerServiceImpl playerService;

    private static final Long GAME_ID = 1L;
    private static final String GAME_NAME = "name";
    private static final Long TOKEN = 12345L;

    private Player playerOne;
    private Long tokenOne = 111L;
    private String nameOne = "David";

    private Player playerTwo;
    private Long tokenTwo = 222L;
    private String nameTwo = "Tom";


    private Player playerThree;
    private Long tokenThree = 333L;
    private String nameThree = "Bořivoj";


    private Player playerFour;
    private Long tokenFour = 444L;
    private String nameFour = "Slavěna";


    // Player not in players
    private Player playerFive;
    private Long tokenFive = 555L;
    private String nameFive = "Anastázie";




    @Mock
    GameRepository gameRepository = Mockito.mock(GameRepository.class);

    @Mock
    PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);

    @Mock
    KillLogRepository killLogRepository = Mockito.mock(KillLogRepository.class);

    @Before
    public void setup(){
        playerService = new PlayerServiceImpl(playerRepository, killLogRepository, gameRepository);
    }

    @Test
    public void creatingGame_createsGame(){
        doAnswer(i -> {
            Game argumentGame = i.getArgument(0);
            if (argumentGame.getId() == null){
                argumentGame.setId(GAME_ID);
            }
            return argumentGame;
        }).when(gameRepository).save(any());


        Game game = playerService.createGame(GAME_NAME);
        assertThat(game.getName()).isEqualTo(GAME_NAME);
        assertThat(game.getActive()).isEqualTo(false);
        assertThat(game.getToken()).isNotNull();
        assertThat(game.getId()).isEqualTo(GAME_ID);
    }


    @Test
    public void startGame_test() throws EmtpyGameException, ModifyingActiveGameException, NotFoundException {
        Game game = new Game();
        game.setId(GAME_ID);
        game.setName(GAME_NAME);
        game.setToken(TOKEN);

        List<Player> players = getPlayers();
        players.forEach(player -> player.setGame(game));

        Mockito.when(gameRepository.findOneByToken(TOKEN)).thenReturn(game);
        Mockito.when(playerRepository.findByGame_Id(GAME_ID)).thenReturn(players);

        // Test
        List<Player> returnedPlayers = playerService.startGame(TOKEN);
        Game returnedGame = returnedPlayers.get(0).getGame();


        // Assertion
        assertThat(returnedGame.getActive()).isEqualTo(true);

        assertThat(returnedPlayers.get(0).getTargetId()).isEqualTo(players.get(1).getPlayerId());
        assertThat(returnedPlayers.get(3).getTargetId()).isEqualTo(players.get(0).getPlayerId());
    }

    @Test
    public void addPlayerToEmptyGame() throws NotFoundException, ModifyingActiveGameException {
        Game game = newGame();
        Mockito.when(gameRepository.findOneByToken(TOKEN)).thenReturn(game);

        Player foundPlayer = playerService.addPlayer(TOKEN, nameFive);
        assertThat(foundPlayer.getName()).isEqualTo(nameFive);
        assertThat(foundPlayer.getGame()).isEqualTo(game);
        assertThat(foundPlayer.getAlive()).isEqualTo(true);
    }


    @Test
    public void addPlayerToActiveGame_throwsException(){
        Game game = newGame();
        game.setActive(true);
        Mockito.when(gameRepository.findOneByToken(TOKEN)).thenReturn(game);

        assertThatThrownBy(() -> playerService.addPlayer(TOKEN, nameFive))
                .isInstanceOf(ModifyingActiveGameException.class);
    }

    @Test
    public void addPlayerToNotExistingGame_throwsException(){
        Game game = newGame();
        game.setActive(true);

        assertThatThrownBy(() -> playerService.addPlayer(TOKEN, nameFive))
                .isInstanceOf(NotFoundException.class);
    }

    private Game newGame() {
        Game game = new Game();
        game.setId(GAME_ID);
        game.setName(GAME_NAME);
        game.setToken(TOKEN);
        return game;
    }


    private List<Player> getPlayers(){
        playerOne = new Player();
        playerOne.setId(1L);
        playerOne.setToken(tokenOne);
        playerOne.setName(nameOne);

        playerTwo = new Player();
        playerTwo.setId(2L);
        playerTwo.setToken(tokenTwo);
        playerTwo.setName(nameTwo);

        playerThree = new Player();
        playerThree.setId(3L);
        playerThree.setToken(tokenThree);
        playerThree.setName(nameThree);

        playerFour = new Player();
        playerFour.setId(4L);
        playerFour.setToken(tokenFour);
        playerFour.setName(nameFour);

        List<Player> players = new ArrayList<>();
        players.add(playerOne);
        players.add(playerTwo);
        players.add(playerThree);
        players.add(playerFour);

        return players;
    }


}
