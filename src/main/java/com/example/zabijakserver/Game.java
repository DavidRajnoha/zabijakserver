package com.example.zabijakserver;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Player> players;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<KillLog> killLogs;


    public Game() {};


    public Game(String name, Set<Player> players) {
        this.players = new HashSet<>(players);
        this.players.forEach(x -> x.setGame(this));
        this.name = name;
    }

    public Game(String name, Player... players) {
        this(name, Stream.of(players).collect(Collectors.toSet()));
    }

    @Override
    public String toString() {
        return "Game Id: " + id;
    }
}
