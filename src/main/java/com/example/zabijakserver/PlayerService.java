package com.example.zabijakserver;

import com.example.zabijakserver.Entities.Game;
import com.example.zabijakserver.Entities.KillLog;
import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Exceptions.*;
import javassist.NotFoundException;
import java.util.List;

public interface PlayerService {

     Player killTarget(Long token) throws PlayerIsNotAliveException, GameIsNotActiveException;

     Player addPlayer(Long gameToken, String name) throws ModifyingActiveGameException, NotFoundException;

     Game createGame(String name);

     List<Player> assignTargets(Long gameToken) throws ModifyingActiveGameException, NotFoundException, EmtpyGameException;

     List<Game> getGames();

     List<Player> getPlayers(Long gameId) throws NotFoundException;

     Game getGame(Long gameId) throws NotFoundException;

     List<KillLog> getKillLog(Long gameId) throws NotFoundException;
}
