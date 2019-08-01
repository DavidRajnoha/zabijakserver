package com.example.zabijakserver.Controllers;

import com.example.zabijakserver.Exceptions.EmtpyGameException;
import com.example.zabijakserver.Exceptions.GameIsNotActiveException;
import com.example.zabijakserver.Exceptions.ModifyingActiveGameException;
import com.example.zabijakserver.Exceptions.PlayerIsNotAliveException;
import com.example.zabijakserver.PlayerService;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class Controller {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private
    PlayerService service;

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

    // PUT MAPPING PUT MAPPING PUT MAPPING

    @PutMapping("/game/{gameToken}/start")
    public @ResponseBody String startGame(@PathVariable Long gameToken) throws JsonProcessingException {
        try {
            return mapper.writerWithView(Views.Player.class).writeValueAsString(service.assignTargets(gameToken));
        } catch (ModifyingActiveGameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't start an active game", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find game based on this token", e);
        } catch (EmtpyGameException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "You can't start game with less then three players");
        }
    }

    @PutMapping("/game/kill/{playerToken}")
    @ResponseBody
    public String killPlayer(@PathVariable(value = "playerToken") Long playerToken) throws JsonProcessingException {
        try {
            return  mapper.writerWithView(Views.Player.class).writeValueAsString(service.killTarget(playerToken));
        } catch (PlayerIsNotAliveException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't kill with not alive player ", e);
        } catch (GameIsNotActiveException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't kill in a inactive game");
        }
    }
    
    //POST MAPPING POST MAPPING POST MAPPING

    @PostMapping("/game/{gameToken}/players/{name}")
    @ResponseBody
    public String addPlayer(@PathVariable(value = "gameToken") Long gameToken, @PathVariable(value = "name") String playerName) throws JsonProcessingException {
        try {
            return mapper.writerWithView(Views.SinglePlayer.class).writeValueAsString(service.addPlayer(gameToken, playerName));
        } catch (ModifyingActiveGameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't modify an active game", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game based on this token could noy be found", e);
        }
    }

    @PostMapping("/game")
    @ResponseBody
    public String createGame(@RequestParam(value = "gameName", required = false, defaultValue = "random name") String gameName
                             //TODO: Parameter passwords to enable connection from other device
    ) throws JsonProcessingException {
        return mapper.writerWithView(Views.GamePrivate.class).writeValueAsString(service.createGame(gameName));
    }
}
