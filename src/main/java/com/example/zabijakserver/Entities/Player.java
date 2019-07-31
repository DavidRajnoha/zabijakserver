package com.example.zabijakserver.Entities;

import com.example.zabijakserver.Views;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;

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

    @ManyToOne
    //Foreign Key
    @JoinColumn
    @JsonManagedReference
    @JsonView(Views.Player.class)
    private Game game;

    protected Player() {
    }

    public Player(String name, Integer playerId) {
        this.name = name;
        this.playerId = playerId;
        this.setAlive(Boolean.TRUE);
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
        this.game = game;
    }
}
