package com.example.Salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Score {


    @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "game_id")
        private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "player_id")
        private Player player;

    private Double score;
    private LocalDateTime finishingDate;

    public Score(){}

    public Score(Double score, LocalDateTime finishingDate){
            this.score = score;
            this.finishingDate = finishingDate;}

    public void setGame (Game game){ this.game = game; }

    public Game getGame ()  { return game; }

    public void setPlayer(Player player){this.player = player;}

    public Player getPlayer(){return player;}

    public void setScore(Double score){this.score = score;}

    public Double getScore(){return score;}

    public void setDate(LocalDateTime finishingDate){this.finishingDate = finishingDate;}

    public LocalDateTime getDate(){return finishingDate;}



}



