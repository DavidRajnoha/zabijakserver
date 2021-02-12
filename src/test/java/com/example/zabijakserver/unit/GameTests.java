package com.example.zabijakserver.unit;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class GameTests {

    private Game testedGame;
    private Long gameId = 1L;
    private String gameName = "Name";

    private Player player;
    private Long playerId = 2L;
    private String playerName = "David";


    @Before
    public void setup(){
        testedGame = new Game();
        testedGame.setId(gameId);
        testedGame.setName(gameName);

        player = new Player();
        player.setId(playerId);
        player.setName(playerName);
    }

    @Test
    public void whenPlayerAddedToGame_thenGameSetToPlayer(){
        testedGame.addPlayer(player);
        assertThat(player.getGame()).isEqualTo(testedGame);
        assertThat(testedGame.getPlayers()).contains(player);
    }

    @Test
    public void whenPlayerRemovedFromGame_thenGameRemovedFromPlayer(){
        testedGame.addPlayer(player);
        assertThat(testedGame.getPlayers()).contains(player);
        testedGame.removePlayer(player);
        assertThat(testedGame.getPlayers()).doesNotContain(player);
        assertThat(player.getGame()).isNotEqualTo(testedGame);
    }
}
