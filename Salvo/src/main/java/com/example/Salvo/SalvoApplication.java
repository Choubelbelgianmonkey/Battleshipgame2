package com.example.Salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {


    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository,
                                      ScoreRepository scoreRepository
    )


    {
        return (args) -> {



            Player p1 = new Player("Jack", "Bauer", "j.bauer@ctu.gov", passwordEncoder().encode("24"));
            playerRepository.save(p1);
            Player p2 = new Player("Chloe", "O'Brian", "c.obrian@ctu.gov", passwordEncoder().encode( "42"));
            playerRepository.save(p2);
            Player p3 = new Player("Kim", "Bauer", "kim_bauer@gmail.com", passwordEncoder().encode("kb"));
            playerRepository.save(p3);
            Player p4 = new Player("Tony", "Almeida", "t.almeida@ctu.gov", passwordEncoder().encode("mole"));
            playerRepository.save(p4);





            Game g1 = new Game(LocalDateTime.now());
            gameRepository.save(g1);
            Game g2 = new Game(LocalDateTime.now().plusHours(1));
            gameRepository.save(g2);
            Game g3 = new Game(LocalDateTime.now().plusHours(2));
            gameRepository.save(g3);
            Game g4 = new Game(LocalDateTime.now().plusHours(3));
            gameRepository.save(g4);
            Game g5 = new Game(LocalDateTime.now().plusHours(4));
            gameRepository.save(g5);
            Game g6 = new Game(LocalDateTime.now().plusHours(5));
            gameRepository.save(g6);
            Game g7 = new Game(LocalDateTime.now().plusHours(6));
            gameRepository.save(g7);
            Game g8 = new Game(LocalDateTime.now().plusHours(7));
            gameRepository.save(g8);


            //game 1
            GamePlayer gp1 = new GamePlayer(g1, p1);
            gamePlayerRepository.save(gp1);
            GamePlayer gp2 = new GamePlayer(g1, p2);
            gamePlayerRepository.save(gp2);

            //game 2
            GamePlayer gp3 = new GamePlayer(g2, p1);
            gamePlayerRepository.save(gp3);
            GamePlayer gp4 = new GamePlayer(g2, p2);
            gamePlayerRepository.save(gp4);

            //game 3
            GamePlayer gp5 = new GamePlayer(g3, p4);
            gamePlayerRepository.save(gp5);
            GamePlayer gp6 = new GamePlayer(g3, p2);
            gamePlayerRepository.save(gp6);

            //game 4
            GamePlayer gp7 = new GamePlayer(g4, p2);
            gamePlayerRepository.save(gp7);
            GamePlayer gp8 = new GamePlayer(g4, p3);
            gamePlayerRepository.save(gp8);

            //game 5
            GamePlayer gp9 = new GamePlayer(g5, p4);
            gamePlayerRepository.save(gp9);
            GamePlayer gp10 = new GamePlayer(g5, p3);
            gamePlayerRepository.save(gp10);

            //game 6
            GamePlayer gp11 = new GamePlayer(g6, p3);
            gamePlayerRepository.save(gp11);

            //game 7
            GamePlayer gp12 = new GamePlayer(g7, p4);
            gamePlayerRepository.save(gp12);

            //game 8
            GamePlayer gp13 = new GamePlayer(g8, p3);
            gamePlayerRepository.save(gp13);
            GamePlayer gp14 = new GamePlayer(g8, p4);
            gamePlayerRepository.save(gp14);


// important to note : as gamePlayer need a game and a player, we need to save those values individually BEFORE, then calling them

            //ships for GAME 1 START here

            List<String> location1 = Arrays.asList("H2", "H3", "H4");
            List<String> location2 = Arrays.asList("E1", "F1", "G1");
            List<String> location3 = Arrays.asList("B4", "B5");
            List<String> location4 = Arrays.asList("B5", "C5", "D5");
            List<String> location5 = Arrays.asList("F1", "F2");


            Ship ship1 = new Ship("Destroyer", location1);
            shipRepository.save(ship1);
            gp1.addShip(ship1);

            Ship ship2 = new Ship("Submarine", location2);
            shipRepository.save(ship2);
            gp1.addShip(ship2);

            Ship ship3 = new Ship("Patrol Boat", location3);
            shipRepository.save(ship3);
            gp1.addShip(ship3);

            Ship ship4  = new Ship("Destroyer", location4);
            shipRepository.save(ship4);
            gp2.addShip(ship4);

            Ship ship5  = new Ship("Patrol Boat", location5);
            shipRepository.save(ship5);
            gp2.addShip(ship5);

            //ships for GAME 1 END here

            //ships for GAME 2 START here

            List<String> location6 = Arrays.asList("B5", "C5", "D5");
            List<String> location7 = Arrays.asList("C7", "C8");
            List<String> location8 = Arrays.asList("A2", "A3", "A4");
            List<String> location9 = Arrays.asList("G6", "H6");

            Ship ship6 = new Ship("Destroyer", location6, gp3);
            shipRepository.save(ship6);

            Ship ship7 = new Ship("Patrol Boat", location7, gp3);
            shipRepository.save(ship7);
            Ship ship8  = new Ship("Submarine", location8, gp4);
            shipRepository.save(ship8);
            Ship ship9  = new Ship("Patrol Boat", location9, gp4);
            shipRepository.save(ship9);

            //ships for GAME 2 END here

            //ships for GAME 3 START here

            //the provided locations are exactly the same as Game2, therefore I copied the location6-9 below

            Ship ship10 = new Ship("Destroyer", location6, gp5);
            shipRepository.save(ship10);
            Ship ship11 = new Ship("Patrol Boat", location7, gp5);
            shipRepository.save(ship11);
            Ship ship12  = new Ship("Submarine", location8, gp6);
            shipRepository.save(ship12);
            Ship ship13  = new Ship("Patrol Boat", location9, gp6);
            shipRepository.save(ship13);

            //ships for GAME 3 END here

            //ships for GAME 4 START here

            //the provided locations are exactly the same as Game3, therefore I copied the location6-9 below

            Ship ship14 = new Ship("Destroyer", location6, gp7);
            shipRepository.save(ship14);
            Ship ship15 = new Ship("Patrol Boat", location7, gp7);
            shipRepository.save(ship15);
            Ship ship16  = new Ship("Submarine", location8, gp8);
            shipRepository.save(ship16);
            Ship ship17  = new Ship("Patrol Boat", location9, gp8);
            shipRepository.save(ship17);

            //ships for GAME 4 END here

            //ships for GAME  START here

            //the provided locations are exactly the same as location6 &  location7

            Ship ship18 = new Ship("Destroyer", location6, gp9);
            shipRepository.save(ship18);
            Ship ship19 = new Ship("Patrol Boat", location7, gp9);
            shipRepository.save(ship19);
            Ship ship20  = new Ship("Submarine", location8, gp10);
            shipRepository.save(ship20);
            Ship ship21  = new Ship("Patrol Boat", location9, gp10);
            shipRepository.save(ship21);

            //ships for GAME 5 END here

            //ships for GAME 6 START here

            //the provided locations are exactly the same as previously, therefore I copied the location6-9 below

            Ship ship22 = new Ship("Destroyer", location6, gp11);
            shipRepository.save(ship22);
            Ship ship23 = new Ship("Patrol Boat", location7, gp11);
            shipRepository.save(ship23);

            //ships for GAME 6 END here

            //ships for GAME 7 START here

            //no data provided for GAME 7

            //ships for GAME 7 END here


            //ships for GAME 8 START here

            //the provided locations are exactly the same as Game3, therefore I copied the location6-9 below

            Ship ship24 = new Ship("Destroyer", location6, gp13);
            shipRepository.save(ship24);
            Ship ship25 = new Ship("Patrol Boat", location7, gp13);
            shipRepository.save(ship25);
            Ship ship26  = new Ship("Submarine", location8, gp14);
            shipRepository.save(ship26);
            Ship ship27  = new Ship("Patrol Boat", location9, gp14);
            shipRepository.save(ship27);

//            //ships for GAME 8 END here

            //salvoes for GAME 1 START here

            Salvo salvo1 = new Salvo(1, Arrays.asList("B5", "C5", "F1"),gp1);
            salvoRepository.save(salvo1);
            Salvo salvo2 = new Salvo (1, Arrays.asList("B4", "B5", "B6"), gp2);
            salvoRepository.save(salvo2);

            Salvo salvo3 = new Salvo(2, Arrays.asList("F2", "D5"), gp1);
            salvoRepository.save(salvo3);
            Salvo salvo4 = new Salvo (2, Arrays.asList("E1", "H3", "A2"), gp2);
            salvoRepository.save(salvo4);

            //salvoes for GAME 1 END here

            //salvoes for GAME 2 START here

            Salvo salvo5 = new Salvo(1, Arrays.asList("A2", "A4", "G6"),gp3);
            salvoRepository.save(salvo5);
            Salvo salvo6 = new Salvo (1, Arrays.asList("B5", "D5", "C7"), gp4);
            salvoRepository.save(salvo6);

            Salvo salvo7 = new Salvo(2, Arrays.asList("A3", "H6"), gp3);
            salvoRepository.save(salvo7);
            Salvo salvo8 = new Salvo (2, Arrays.asList("C5", "C6"), gp4);
            salvoRepository.save(salvo8);

            //salvoes for GAME 2 END here

            //salvoes for GAME 3 START here

            Salvo salvo9 = new Salvo(1, Arrays.asList("G6", "H6", "A4"),gp5);
            salvoRepository.save(salvo9);
            Salvo salvo10 = new Salvo (1, Arrays.asList("H1", "H2", "H3"), gp6);
            salvoRepository.save(salvo10);

            Salvo salvo11 = new Salvo(2, Arrays.asList("A2", "A3", "D8"), gp5);
            salvoRepository.save(salvo11);
            Salvo salvo12 = new Salvo (2, Arrays.asList("E1", "F2", "G3"), gp6);
            salvoRepository.save(salvo12);

            //salvoes for GAME 3 END here

            //salvoes for GAME 4 START here

            Salvo salvo13 = new Salvo(1, Arrays.asList("A3", "A4", "F7"),gp7);
            salvoRepository.save(salvo13);
            Salvo salvo14 = new Salvo (1, Arrays.asList("B5", "C6", "H1"), gp8);
            salvoRepository.save(salvo14);

            Salvo salvo15 = new Salvo(2, Arrays.asList("A2", "G6", "H6"), gp7);
            salvoRepository.save(salvo15);
            Salvo salvo16 = new Salvo (2, Arrays.asList("C5", "C7", "D5"), gp8);
            salvoRepository.save(salvo16);

            //salvoes for GAME 4 END here

            //salvoes for GAME 5 START here

            Salvo salvo17 = new Salvo(1, Arrays.asList("A1", "A2", "A3"),gp9);
            salvoRepository.save(salvo17);
            Salvo salvo18 = new Salvo (1, Arrays.asList("B5", "B6", "C7"), gp10);
            salvoRepository.save(salvo18);

            Salvo salvo19 = new Salvo(2, Arrays.asList("G6", "G7", "G8"), gp9);
            salvoRepository.save(salvo19);
            Salvo salvo20 = new Salvo (2, Arrays.asList("C6", "D6", "E6"), gp10);
            salvoRepository.save(salvo20);

            Salvo salvo21 = new Salvo(3, Arrays.asList("H1", "H8"), gp10);
            salvoRepository.save(salvo21);

            //salvoes for GAME 5 END here


            //Score BEGIN here, i use the "addXX" method for this step, note this method had to be added on both template Game and Player to be valid here

            Score sc1 = new Score(1.0,LocalDateTime.now().plusMinutes(30));
            p1.addScore(sc1);
            g1.addScore(sc1);
            scoreRepository.save(sc1);

            Score sc2 = new Score(0.0, LocalDateTime.now().plusMinutes(30));
            p2.addScore(sc2);
            g1.addScore(sc2);
            scoreRepository.save(sc2);

            Score sc3 = new Score(0.5, LocalDateTime.now().plusHours(1).plusMinutes(30));
            p1.addScore(sc3);
            g2.addScore(sc3);
            scoreRepository.save(sc3);

            Score sc4 = new Score(0.5, LocalDateTime.now().plusHours(1).plusMinutes(30));
            p2.addScore(sc4);
            g2.addScore(sc4);
            scoreRepository.save(sc4);

            Score sc5 = new Score(1.0, LocalDateTime.now().plusHours(2).plusMinutes(30));
            p2.addScore(sc5);
            g3.addScore(sc5);
            scoreRepository.save(sc5);

            Score sc6 = new Score(0.0, LocalDateTime.now().plusHours(2).plusMinutes(30));
            p4.addScore(sc6);
            g3.addScore(sc6);
            scoreRepository.save(sc6);

            Score sc7 = new Score(0.5, LocalDateTime.now().plusHours(3).plusMinutes(30));
            p2.addScore(sc7);
            g4.addScore(sc7);
            scoreRepository.save(sc7);

            Score sc8 = new Score(0.5, LocalDateTime.now().plusHours(3).plusMinutes(30));
            p1.addScore(sc8);
            g4.addScore(sc8);
            scoreRepository.save(sc8);

            Score sc9 = new Score(0.0, LocalDateTime.now().plusHours(4).plusMinutes(30));
            p4.addScore(sc9);
            g5.addScore(sc9);
            scoreRepository.save(sc9);

            Score sc10 = new Score(1.0, LocalDateTime.now().plusHours(4).plusMinutes(30));
            p1.addScore(sc10);
            g5.addScore(sc10);
            scoreRepository.save(sc10);

            Score sc11 = new Score(1.0, LocalDateTime.now().plusHours(5).plusMinutes(30));
            p3.addScore(sc11);
            g6.addScore(sc11);
            scoreRepository.save(sc11);

            Score sc12 = new Score(0.0, LocalDateTime.now().plusHours(6).plusMinutes(30));
            p4.addScore(sc12);
            g7.addScore(sc12);
            scoreRepository.save(sc12);

            Score sc13 = new Score(1.0, LocalDateTime.now().plusHours(7).plusMinutes(30));
            p3.addScore(sc13);
            g8.addScore(sc13);
            scoreRepository.save(sc13);

            Score sc14 = new Score(0.0, LocalDateTime.now().plusHours(7).plusMinutes(30));
            p4.addScore(sc14);
            g8.addScore(sc14);
            scoreRepository.save(sc14);



        };
    }
}


@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepo.findByUsername(inputName);
            if (player != null) {
                return new User(player.getUsername(), player.getPassword(), //carefull here user is not a gameplayer, but a ROLE given to the gp (user, admin, etc	)
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/game.html").hasAuthority("USER")
                .antMatchers("/api").permitAll()
                .and()
                .formLogin();

        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}