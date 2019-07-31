package com.example.zabijakserver.Controllers;

import com.example.zabijakserver.Exceptions.ModifyingActiveGameException;
import com.example.zabijakserver.PlayerService;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import com.example.zabijakserver.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private
    PlayerService service;

    @Autowired
    private
    PlayerRepository playerRepository;

    private
    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/game/{gameId}")
    public String game(@PathVariable(value = "gameId") Long gameId) throws JsonProcessingException {
            return mapper.writerWithView(Views.Game.class).writeValueAsString(service.getGame(gameId));
    }

    @GetMapping("/game")
    @ResponseBody
    public String getGames() throws JsonProcessingException {
        return mapper.writerWithView(Views.Game.class).writeValueAsString(service.getGames());

    }

    @GetMapping("/game/create")
    @ResponseBody
    public String createGame(@RequestParam(value = "gameName", required = false, defaultValue = "random name") String gameName,
                             @RequestParam(value = "names", defaultValue = "testing,set,of,names") List<String> names
                            //TODO: Parameter passwords to enable connection from other device
                             ) throws JsonProcessingException {

            return mapper.writerWithView(Views.Game.class).writeValueAsString(service.createGame(gameName, names));

    }

    @GetMapping("/game/{gameId}/add/{name}")
    @ResponseBody
    public String addPlayer(@PathVariable(value = "gameId") Long gameId, @PathVariable(value = "name") String playerName) throws JsonProcessingException {
        try {
            return mapper.writerWithView(Views.Player.class).writeValueAsString(service.addPlayer(gameId, playerName));
        } catch (ModifyingActiveGameException e) {
            return "You can not add player to an active game";
        }
    }


    @GetMapping("/game/{gameId}/kill/{playerId}")
    @ResponseBody
    public String killPlayer(@PathVariable(value = "gameId") Long gameId,
                             @PathVariable(value = "playerId") Integer playerId) throws JsonProcessingException {
         return  mapper.writerWithView(Views.Player.class).writeValueAsString(service.killTarget(gameId, playerId));
    }


    @GetMapping("/game/{gameId}/players")
    @ResponseBody
    public String getPlayers(@PathVariable Long gameId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithView(Views.Player.class).writeValueAsString(service.getPlayers(gameId));
    }



    @GetMapping("/game/{gameId}/start")
    public @ResponseBody String startGame(@PathVariable Long gameId) throws JsonProcessingException {
        service.assignTargets(gameId);
        return mapper.writerWithView(Views.Player.class).writeValueAsString(service.getPlayers(gameId));
    }

    @GetMapping("/game/{gameId}/killlogs")
    @ResponseBody
    public String getGameKilllogs(@PathVariable() Long gameId) throws JsonProcessingException {
            return mapper.writeValueAsString(service.getKillLog(gameId));
    }
}
