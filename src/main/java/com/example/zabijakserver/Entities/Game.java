package com.example.zabijakserver.Entities;

import com.example.zabijakserver.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize
    @JsonView({Views.Player.class, Views.Game.class})
    private Long id;

    @JsonView({Views.Player.class, Views.Game.class})
    private String name;

    @JsonView({Views.Player.class, Views.Game.class})
    private Timestamp created;

    @JsonView({Views.Player.class, Views.Game.class})
    private Boolean active;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @JsonView(Views.Game.class)
    private Set<Player> players;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    @JsonView(Views.Game.class)
    private Set<KillLog> killLogs;


    public Game() {};


    public Game(String name, Set<Player> players) {
        this.players = new HashSet<>(players);
        this.players.forEach(x -> x.setGame(this));
        this.name = name;
        this.active = false;
        this.killLogs = new HashSet<>();
    }

    public Game(String name, Player... players) {
        this(name, Stream.of(players).collect(Collectors.toSet()));
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<KillLog> getKillLogs() {
        return killLogs;
    }

    public void addKillLog(KillLog killLog) {
        this.killLogs.add(killLog);
    }
}
