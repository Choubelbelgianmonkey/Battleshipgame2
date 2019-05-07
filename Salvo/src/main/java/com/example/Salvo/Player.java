package com.example.Salvo;


import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();
//do not need THIS (cf below) as we define keys, not values

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public Player() {
    }

    public Player(String first, String last, String email, String password) {
        this.firstName = first;
        this.lastName = last;
        this.username = email;
        this.password = password;
    }

//    this must ALWAYS be used to set up value on the defined keys

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){return password;}

    public void setPassword(String password){this.password = password;}

    public Set<Score> getScore(){return scores;}

    public void setScore(){this.scores = scores;}

    public void addScore(Score score) {
        score.setPlayer(this);
        scores.add(score);
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                '}';
// pay attention to the ID and first ad username it all should follow the same logic
    }

    public Set<Score> getScore(Game game){ return game.getScore(); }

}
