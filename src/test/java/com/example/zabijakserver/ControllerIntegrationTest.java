package com.example.zabijakserver;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Repositories.GameRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
@ComponentScan("com.example.zabijakserver")
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlayerServiceImpl playerService;


    @Autowired
    private GameRepository gameRepository;

    @Before
    public void setUp() {


        Integer playerId = 1;
        Game game = new Game();
        gameRepository.save(game);
        Player player = new Player("Player", playerId, game);
        player.setGame(game);

    }


}
