package com.example.zabijakserver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private String name;
    private Long targetId;
    private Boolean isAlive = Boolean.TRUE;

    @ManyToOne
    //Foreign Key
    @JoinColumn
    private Game game;

    protected Player() {
    };

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, Long playerId, Long targetId) {
        this(name);
        this.targetId = targetId;
        this.playerId = playerId;
    }


    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetId() {
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


    @Override
    public String toString() {
        return String.format("Player[id='%d', playerId='%d', firstName='%s', target='%d', game='%s']",
                id, playerId, name, targetId, game);
    }


    public Long getPlayerId() {
        return playerId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
