package com.example.Salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Long turnNumber;

    @ElementCollection
    @Column(name="locationSalvo")
    private List<String> locationSalvo = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameplayer_id")
    private GamePlayer gamePlayer;


    public Salvo(){}

    public Salvo(long turnNumber, List<String> locationSalvo, GamePlayer gamePlayer) {
        this.turnNumber = turnNumber;
        this.locationSalvo = locationSalvo;
        this.gamePlayer = gamePlayer;
    }

    public Salvo(long turnNumber, List<String> locationSalvo) {
        this.turnNumber = turnNumber;
        this.locationSalvo = locationSalvo;
    }

    public Salvo(List<String> locationSalvo){
        this.locationSalvo = locationSalvo;
    }



    public GamePlayer getGamePlayer(){
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }



    public Long getTurnNumber(){
        return turnNumber;
    }

    public void setTurnNumber(Long turnNumber){
        this.turnNumber = turnNumber;
    }

    public List<String> getLocationSalvo(){
        return locationSalvo;
    }

    public void setLocationSalvo(List<String> locationSalvo){
        this.locationSalvo = locationSalvo;
    }

}
