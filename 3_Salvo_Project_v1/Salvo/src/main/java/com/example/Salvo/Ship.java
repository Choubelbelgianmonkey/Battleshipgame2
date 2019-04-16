package com.example.Salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
public class Ship {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String type;


    @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name="gameplayer_id")
        private GamePlayer gamePlayer;

        @ElementCollection
        @Column(name="location")
        private List<String> location = new ArrayList<>();

    public Ship() {
        }

    public Ship(String type, List<String> location, GamePlayer gamePlayer) {
            this.type = type;
            this.location = location;
            this.gamePlayer = gamePlayer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public GamePlayer getGamePlayer(){

        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer){

       this.gamePlayer = gamePlayer;
    }

}
