package com.example.zabijakserver.integration;

import com.example.zabijakserver.Controllers.Controller;
import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.PlayerServiceImpl;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = Controller.class)
@ComponentScan("com.example.zabijakserver")
public class ControllerIntegrationTest {

    @Autowired
       private MockMvc mvc;

    @MockBean
    private PlayerServiceImpl playerService;



    private List<Game> games;
    private Game game;

//    @Before
//    public void setUp() {
//        Integer playerId = 1;
//        game = new Game();
//        game.setId(1L);
//        Player player = new Player("Player", playerId, game);
//        player.setGame(game);
//
//        games = Collections.singletonList(game);
//
//        Mockito.when(playerService.getGames()).thenReturn(games);
//    }

    @Test
    public void contextLoads() {
    }

//    @Test
//    public void whenCallingGameWithoutParameterThenReturnJsonArrayOfGames() throws Exception {
//        mvc.perform(get("/game/")
//            .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", hasSize(1)))
////            .andExpect(jsonPath("$[0].id", is(game.getId())));
//        ;
//    }


}
