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

//TODO: rewrite uri paths to the type /game/{gameId}/action

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

    @RequestMapping("/game")
    public String game(@RequestParam(value = "id", defaultValue = "1") Long id) throws JsonProcessingException {
            return mapper.writerWithView(Views.Game.class).writeValueAsString(service.getGame(id));
    }

    @GetMapping("/games")
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

    @GetMapping("/game/add")
    @ResponseBody
    public String addPlayer(@RequestParam(value = "gameId") Long gameId, @RequestParam(value = "name") String playerName) throws JsonProcessingException {
        try {
            return mapper.writerWithView(Views.Player.class).writeValueAsString(service.addPlayer(gameId, playerName));
        } catch (ModifyingActiveGameException e) {
            return "You can not add player to an active game";
        }
    }



    /*TODO: Take Json as a parameter and change method type to POST*/
    @GetMapping("/game/kill")
    @ResponseBody
    public String killPlayer(@RequestParam(value = "gameId", defaultValue = "1") Long gameId,
                             @RequestParam(value = "playerId", defaultValue = "0") Integer playerId) throws JsonProcessingException {
         return  mapper.writerWithView(Views.Player.class).writeValueAsString(service.killTarget(gameId, playerId));
    }


    @GetMapping("/game/players")
    @ResponseBody
    public String getPlayers(Long gameId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithView(Views.Player.class).writeValueAsString(service.getPlayers(gameId));
    }



    @GetMapping("/game/start")
    public @ResponseBody String startGame(Long gameId) throws JsonProcessingException {
        service.assignTargets(gameId);
        return mapper.writerWithView(Views.Player.class).writeValueAsString(service.getPlayers(gameId));
    }

    @GetMapping("/game/killlogs")
    @ResponseBody
    public String getKilllogs(@RequestParam(defaultValue = "-1") Long gameId) throws JsonProcessingException {
        if (gameId == -1L){
            return mapper.writeValueAsString(service.getKilllogs());
        } else {
            return mapper.writeValueAsString(service.getKillLog(gameId));
        }
    }

}
