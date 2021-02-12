package com.example.zabijakserver.unit;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.Player;
import org.junit.Before;
import org.junit.Test;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PlayerTest {
    private String playerName = "david";
    private int playerId = 1;
    private String gameOneName = "Game One";
    private Game gameOne = new Game();


    @Before
    public void setUp() {
        gameOne.setId(1L);
        gameOne.setName(gameOneName);
    }

    @Test
    public void whenCreatingPlayerValidTokenShouldBeCreated() {
        Player player = new Player(playerName, playerId, gameOne);
        Long token = player.getToken();
        String expectedToken = String.valueOf(gameOne.getId()) + playerId;
        assertThat(String.valueOf(token)).contains(expectedToken);
    }

    @Test
    public void whenGameSetToPlayer_thenPlayerSetToGame() {
        Player player = new Player();
        player.setGame(gameOne);

        assertThat(gameOne.getPlayers()).contains(player);
    }
}
