package com.example.zabijakserver.Entities;

import com.example.zabijakserver.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Entity
public class Game implements Serializable {

    //TODO: Modify the setters and getters in relationships to ensure consistency!!!!

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
    private Boolean active = false;

    @OneToMany(targetEntity = Player.class, fetch = FetchType.LAZY, mappedBy = "game", cascade = CascadeType.ALL)
    @JsonView(Views.Game.class)
    private List<Player> players = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    @JsonView(Views.Game.class)
    private List<KillLog> killLogs = new ArrayList<>();

    @JsonView(Views.GamePrivate.class)
    private Long token;


    public Game() {};

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void addKillLog(KillLog killLog) {

        this.killLogs.add(killLog);
    }

    public Long getId() {
        return id;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(){
        this.token = Long.parseLong(String.valueOf(this.id) + (new Random().nextInt(9000)+1000));
    }

    public void addPlayer(Player player){
        if (this.players == null){
            this.players = new ArrayList<>();
        }

        if (this.players.contains(player)) return;

        this.players.add(player);

        player.setGame(this);
    }

    public void removePlayer(Player player){
        if (this.players == null || !this.players.contains(player)) return;

        this.players.remove(player);

        player.setGame(null);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<KillLog> getKillLogs() {
        return killLogs;
    }

    public void setToken(Long id){
        this.token = Long.parseLong(String.valueOf(id) + (new Random().nextInt(9000)+1000));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getName() {
        return this.name;
    }
}
