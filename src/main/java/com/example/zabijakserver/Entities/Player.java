package com.example.zabijakserver.Entities;

import com.example.zabijakserver.Views;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

@Entity
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Player.class, Views.Game.class})
    private Long id;

    @JsonView({Views.Player.class, Views.Game.class})
    private Integer playerId;

    @JsonView({Views.Player.class, Views.Game.class})
    private String name;

    @JsonView({Views.Player.class, Views.Game.class})
    private Integer targetId;

    @JsonView({Views.Player.class, Views.Game.class})
    private Boolean isAlive = Boolean.TRUE;

    @JsonView({Views.Player.class, Views.Game.class})
    private Boolean isWinner = Boolean.FALSE;

    @ManyToOne (targetEntity = Game.class, fetch = FetchType.LAZY)
    //Foreign Key
    @JsonManagedReference
    @JsonView(Views.Player.class)
    private Game game;

    @JsonView(Views.SinglePlayer.class)
    Long token;

    public Player() {
    }


    public Player(String name, Integer playerId, Game game) {
        this.name = name;
        this.playerId = playerId;
        this.setAlive(Boolean.TRUE);
        this.setGame(game);
        this.token = Long.parseLong(String.valueOf(game.getId()) +
                playerId +
                (new Random().nextInt(900) + 100));
    }

    public Player(String name, Integer playerId, Game game, Boolean bool) {
        this.name = name;
        this.playerId = playerId;
        this.setAlive(Boolean.TRUE);
        this.setGame(game);

    }



    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public String getName() {
        return name;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        if (Objects.equals(this.game, game)) return;

        if (this.game != null) this.game.removePlayer(this);

        this.game = game;

        if (this.game != null) this.game.addPlayer(this);
    }

    public Long getToken() {
        return token;
    }

    //Just for testing

    public void setToken(Long token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String playerName) {
        this.name = name;
    }

    public Boolean getWinner() {
        return isWinner;
    }

    public void setWinner(Boolean winner) {
        isWinner = winner;
    }
}
