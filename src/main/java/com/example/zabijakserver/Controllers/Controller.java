package com.example.zabijakserver.Controllers;

import com.example.zabijakserver.Exceptions.ModifyingActiveGameException;
import com.example.zabijakserver.Exceptions.PlayerIsNotAliveException;
import com.example.zabijakserver.PlayerService;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import com.example.zabijakserver.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/game/{gameId}")
    public String game(@PathVariable(value = "gameId") Long gameId) throws JsonProcessingException {
        try {
            return mapper.writerWithView(Views.Game.class).writeValueAsString(service.getGame(gameId));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID: "+gameId+" does not exist", e);
        }
    }

    @GetMapping("/game")
    @ResponseBody
    public String getGames() throws JsonProcessingException {
        return mapper.writerWithView(Views.Game.class).writeValueAsString(service.getGames());

    }


    @GetMapping("/game/{gameId}/players")
    @ResponseBody
    public String getPlayers(@PathVariable Long gameId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithView(Views.Player.class).writeValueAsString(service.getPlayers(gameId));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID: "+gameId+" does not exist", e);
        }
    }

    @GetMapping("/game/{gameId}/killlogs")
    @ResponseBody
    public String getGameKilllogs(@PathVariable() Long gameId) throws JsonProcessingException {
        try {
            return mapper.writeValueAsString(service.getKillLog(gameId));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID: "+gameId+" does not exist", e);
        }
    }



    @PutMapping("/game/{gameId}/start")
    public @ResponseBody String startGame(@PathVariable Long gameId) throws JsonProcessingException {
        try {
            return mapper.writerWithView(Views.Player.class).writeValueAsString(service.assignTargets(gameId));
        } catch (ModifyingActiveGameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't start an active game", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID: "+gameId+" does not exist", e);
        }
    }

    @PutMapping("/game/{gameId}/kill/{playerId}")
    @ResponseBody
    public String killPlayer(@PathVariable(value = "gameId") Long gameId,
                             @PathVariable(value = "playerId") Integer playerId) throws JsonProcessingException {

        try {
            return  mapper.writerWithView(Views.Player.class).writeValueAsString(service.killTarget(gameId, playerId));
        } catch (PlayerIsNotAliveException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't kill with not alive player ", e);
        }
    }

    @PostMapping("/game/{gameId}/players/{name}")
    @ResponseBody
    public String addPlayer(@PathVariable(value = "gameId") Long gameId, @PathVariable(value = "name") String playerName) throws JsonProcessingException {
        try {
            return mapper.writerWithView(Views.Player.class).writeValueAsString(service.addPlayer(gameId, playerName));
        } catch (ModifyingActiveGameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't modify an active game", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with ID: "+gameId+" does not exist", e);
        }
    }

    @PostMapping("/game")
    @ResponseBody
    public String createGame(@RequestParam(value = "gameName", required = false, defaultValue = "random name") String gameName,
                             @RequestParam(value = "names", required = false) List<String> names
                             //TODO: Parameter passwords to enable connection from other device
    ) throws JsonProcessingException {
        return mapper.writerWithView(Views.Game.class).writeValueAsString(service.createGame(gameName, names));
    }
}
