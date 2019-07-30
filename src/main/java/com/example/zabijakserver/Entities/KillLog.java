package com.example.zabijakserver.Entities;

import com.example.zabijakserver.Entities.Game;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class KillLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;
    @JsonProperty
    private Integer killerId;
    @JsonProperty
    private Integer targetId;
    @JsonProperty
    private Timestamp killTime;

    @ManyToOne
    //Foreign Key
    @JoinColumn
    @JsonIgnore
    private Game game;


    public KillLog() {};

    public KillLog(Game game, Integer killerId, Integer targetId) {
        this.game = game;
        this.killerId = killerId;
        this.targetId = targetId;
        this.killTime =  new Timestamp(new Date().getTime());
    }

    @Override
    public String toString() {
        return "Player with ID: " + killerId + " has killed his target with ID: " + targetId + " on " + killTime + " in game: "+game;
    }
}
