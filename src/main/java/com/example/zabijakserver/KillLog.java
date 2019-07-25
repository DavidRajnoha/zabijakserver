package com.example.zabijakserver;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

import java.util.Set;
import java.util.Timer;

@Entity
public class KillLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long killerId;
    private Long targetId;
    private Timestamp killTime;

    @ManyToOne
    //Foreign Key
    @JoinColumn
    private Game game;


    public KillLog() {};

    public KillLog(Game game, Long killerId, Long targetId) {
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
