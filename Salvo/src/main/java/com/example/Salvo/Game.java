package com.example.Salvo;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.lang.Long;

import java.util.HashSet;
import java.util.Set;




@Entity
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime date;
//    private Long EpochDate;it was fun to do, but useless

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "game", fetch= FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();;

    public Game() {
    }

    public Game(LocalDateTime date) {
        this.date = date;
//        this.EpochDate = EpochDate;it was fun to do, but useless
   }

//    public void setEpochDate() {
//        this.EpochDate = EpochDate;
//
//    }it was fun to do, but useless

//    public Long getEpochDate() {
//        return System.currentTimeMillis();
//    }it was fun to do, but useless

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<GamePlayer> getPlayers() {
        return this.gamePlayers;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setScore (Score score){this.scores = scores;}

    public Set<Score>  getScore () {return scores;}

    public void addScore(Score score) {
        score.setGame(this);
       scores.add(score);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                "date is " + date +
                '}';
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }





}
