package com.example.Salvo;


import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

import java.lang.Long;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;


@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch= FetchType.EAGER)
    private Set<Ship> ships = new LinkedHashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch= FetchType.EAGER)
    private Set<Salvo> salvoes = new LinkedHashSet<>();


    public GamePlayer() {}


    public GamePlayer(Game game_id, Player player_id) {
        this.game = game_id;
        this.player = player_id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer(){return player;}

    public void setPlayer(Player player){this.player = player;}


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Ship> getShips() {
        return this.ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        this.ships.add(ship);
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", game='" + game + '\'' +
                ", player='" + player + '\'' +
                '}';
    }

    public Score getScore(Score score){

        return score;
    }

}
