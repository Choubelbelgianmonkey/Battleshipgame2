package com.example.Salvo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;



//    package com.LinkedLists;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class SalvoControler {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private  PlayerRepository playerRepo;

    @Autowired
    private  GamePlayerRepository gamePlayerRepo;

    @Autowired
    private  ShipRepository shipRepo;

    @Autowired
    private SalvoRepository salvoRepo;

    //get game info

    @RequestMapping("/games2")
        public Map<String, Object> getGamesPerPlayer(Authentication authentication){

            Map<String, Object> game2DTO = new HashMap<>();

        if (authentication != null){
            game2DTO.put("player", playerInfo(getLoggedPlayer(authentication)));
        } else {
        game2DTO.put("player", "guest");}
        game2DTO.put("games", gameRepo

                    .findAll()
                    .stream()
                    .map(game -> getGameInfo(game))
                    .collect(toList()));

        return game2DTO;
        }

        //create new game
        @PostMapping("/games2")
        ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

                Game newGame = new Game(LocalDateTime.now());
                Player newPlayer = getLoggedPlayer(authentication);
                GamePlayer newGamePlayer = new GamePlayer(newGame, newPlayer);
                gameRepo.save(newGame);
                gamePlayerRepo.save(newGamePlayer);
                return new ResponseEntity<>(sendInfo("gpid", newGamePlayer.getId()), HttpStatus.CREATED);

        }

        //join game
        @PostMapping("/game/{game_id}/players")
            ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long game_id, Authentication authentication) {

            if (authentication == null) {
                return new ResponseEntity<>(sendInfo("error", "You need to be logged in to join a Game! Please Log in or Sign up."), HttpStatus.UNAUTHORIZED);
            }

            Game currentGame = gameRepo.findById(game_id).orElse(null);

            if (currentGame == null) {
                return new ResponseEntity<>(sendInfo("error", "No such game"), HttpStatus.FORBIDDEN);
            }
            if (currentGame.getGamePlayers().size() != 1) {
                return new ResponseEntity<>(sendInfo("error", "Game is already full"), HttpStatus.FORBIDDEN);
            }

            Player currentPlayer = getLoggedPlayer(authentication);

            for (GamePlayer gamePlayer : currentGame.getGamePlayers()) { //is the same as foreach in javascript because its en o
                if (gamePlayer.getPlayer().equals(currentPlayer))
                      return new ResponseEntity<>(sendInfo("error", "You are already there!"), HttpStatus.CONFLICT); ;
            }

            GamePlayer newGamePlayer = new GamePlayer(currentGame, currentPlayer);
            gamePlayerRepo.save(newGamePlayer);
            return new ResponseEntity<>(sendInfo("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
        }



        public Player getLoggedPlayer(Authentication authentication){
        if (authentication != null){
            return playerRepo.findByUsername(authentication.getName());
        } else{
            return null;
        }
      }
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/games")
    public List<Object> getAllGames(){

        return gameRepo
                .findAll()
                .stream()
                .map(game -> getGameInfo(game))
                .collect(toList());
    }

    private Map<String,Object> getGameInfo(Game game){
        Map<String,Object> gameInfo  = new HashMap<>();

        gameInfo.put("id", game.getId());
        gameInfo.put("date", game.getDate());
//        gameInfo.put("EpochTime", game.getEpochDate()); //it was fun to do, but useless
        gameInfo.put("gamePlayer", game.getGamePlayers()//this calll for the gameplayer in game
                                    .stream()
                                    .map(gamePlayer -> getGPInfo(gamePlayer))//this will return the specific info called 2 lines above
                                    .collect(Collectors
                                            .toList()));

        gameInfo.put("score", game.getScore()
                .stream()
                .map(score -> scoreDTO(score))
                .collect(Collectors
                        .toList()));

        return gameInfo;
    }

    private Map<String, Object> getGPInfo(GamePlayer gamePlayer){

        Map<String,Object> GPInfo = new HashMap<>();

        GPInfo.put("id", gamePlayer.getId());
        GPInfo.put("player", playerInfo(gamePlayer.getPlayer())); //the game is already set above, but we still need to get playerinfo below:

       return  GPInfo;
    }

    private Double scoreDTO(Score score){

        return score.getScore();
    }

    private Map<String, Object> playerInfo(Player player){

        Map<String, Object> playerInfo = new HashMap<>();

        playerInfo.put("id",player.getId());
        playerInfo.put("username", player.getUsername());
        playerInfo.put("score", player.getScore()
                                .stream()
                                .map(score -> scoreDTO(score))
                                .collect(Collectors
                                 .toList())
                                );

        return playerInfo; //here we received and take the specifics info from the function gamePlayer.getPlayer()
    }

    @RequestMapping("/leaderboard")
    public Map<String, Object> getLeaderoardContent(){
        Map<String, Object> scores = new HashMap<>();

        scores.put("score", playerRepo.findAll()
                .stream()

                .map(player -> playerInfo(player))
                .sorted((e1, e2) -> {//each player from playerRepo are either e1 or e2, e2 being the player with higher output (output will be define below)

                    List<Double> list1= (ArrayList) e1.get("score"); //this change the "map <string object>" in a list of double ...
                    List<Double> list2= (ArrayList) e2.get("score"); //...  it is a requirement to use .sum() (its normal as "object" could also be a string)

                    Double total1 =list1.stream().mapToDouble(f -> f.doubleValue()).sum(); //thanks to the list above, we can sum it up ...
                    Double total2 =list2.stream().mapToDouble(f -> f.doubleValue()).sum(); //... and now we can compare them


                    Integer sComp = total2.compareTo(total1); //sComp is then -1, 0 or 1

                    if (sComp != 0) {//if the Scomp does not return an equal value(0), just return the sort the player as set up ...
                        return sComp; //...this return only (1, -1)
                    }
                    Integer count1 = ((ArrayList) e1.get("score")).size(); //setting up the value to compare below ...
                    Integer count2 = ((ArrayList) e2.get("score")).size(); // here is only the size, hence no need to map: counting the amount of stuff ...
                                                                            // in an array is different from sum(), as we dont need to enter them

                    return count1.compareTo(count2); //now, for any other possiblities as the if statement above (so 1 and -1) compare the size()

                })
        .collect(toList()));

    return scores;

    }


    @GetMapping("/game_view/{GamePlayerID}")
    public ResponseEntity<Map<String, Object>> checkUSerCompliance(@PathVariable Long GamePlayerID, Authentication authentication) {

        GamePlayer gamePlayer = gamePlayerRepo.getOne(GamePlayerID);

        if (gamePlayer.getPlayer().getId() != getLoggedPlayer(authentication).getId()) {

            return new ResponseEntity<>(sendInfo("error","Dont try to cheat!" ), HttpStatus.UNAUTHORIZED);

            //here sendInfo is a method created below which will transform the response to a type accepable by "ResponseEntity<Map<String, Object>>"
        } else {
            return new ResponseEntity<>(gameViewId(GamePlayerID), HttpStatus.OK);
        }
    }

    public Map<String, Object> gameViewId(@PathVariable Long GamePlayerID)  {
        Map<String, Object> gameViewDTO = new LinkedHashMap<>();
      // Optional<GamePlayer> gamePlayer = gamePlayerRepo.findById(GamePlayerID);
        //optional tell me that the value might be null
       GamePlayer gamePlayer = gamePlayerRepo.getOne(GamePlayerID);

       gameViewDTO.put("game", getGameViewInfo(gamePlayer));

        return gameViewDTO;
        }

    private Map<String,Object> getGameViewInfo(GamePlayer gamePlayer){
        Map<String,Object> gameInfo  = new HashMap<>();

        gameInfo.put("id",gamePlayer.getGame().getId());
        gameInfo.put("date", gamePlayer.getGame().getDate());
//        gameInfo.put("EpochTime", game.getEpochDate()); //it was fun to do, but useless
        gameInfo.put("gamePlayer", gamePlayer.getGame().getGamePlayers()//this calll for the gameplayer in game
                // the line above show we are getting the gameplayer matching the game
                .stream()
                .map(gp -> getGPInfo(gp))//this will return the specific info called 3 lines above
                .collect(Collectors
                        .toList()));
        gameInfo.put("ships", gamePlayer.getShips()
                        .stream()
                        .map(ship -> makeShipDto(ship))
                        .collect(toList()));
        gameInfo.put("mySalvoes", gamePlayer.getSalvoes()
                            .stream()
                            .map(salvo -> makeSalvoDto(salvo))
                            .collect(toList()));
        if (getOpponent(gamePlayer) != null){
        gameInfo.put("hisSalvoes", getOpponent(gamePlayer).getSalvoes()
                .stream()
                .map(salvo -> makeSalvoDto(salvo))
                .collect(toList()));
        }
        return gameInfo;
    }

    private Map<String, Object> makeShipDto(Ship ship) {
        Map<String,Object> shipDTO  = new HashMap<>();
        shipDTO.put("type", ship.getType());
        shipDTO.put("location", ship.getLocation());

        return shipDTO;
    }

private Map<String, Object> makeSalvoDto (Salvo salvo){

        Map<String, Object> salvoDTO = new HashMap<>();

    salvoDTO.put("turn",salvo.getTurnNumber());
    salvoDTO.put("location", salvo.getLocationSalvo());

        return salvoDTO;
}

    private GamePlayer getOpponent(GamePlayer gamePlayer){


//        GamePlayer opponent = new HashMap<>();

        return gamePlayer.getGame().getGamePlayers()
                    .stream()
                    .filter(gamePlayer1 -> !gamePlayer
                            .getId()
                            .equals(gamePlayer1.getId()))
                    .findFirst()
                    .orElse(null);

            //this will get the called gampeplayer (between ()) sorted by games, then filter them (while calling each one gamaplayer1)
            //for each gameplayer1, if the ID of the called gameplayer is not equal to the id of the gameplayer1
        //you return it, if you dont find it(EG: only one player) return null

    }

    //to GET a list of player on the URL /API/PLAYERS
    @GetMapping("/players")
    List<Object> getPlayersInfo() {
        Set<Player> players = new LinkedHashSet<>(playerRepo
                                                .findAll());
        return players
                .stream()
                .map(this::playerInfo)
                .collect(toList());
    }
    @Autowired
    PasswordEncoder passwordEncoder;

    //to POST it will create the player a player on the URL /API/PLAYERS
    @PostMapping("/players")
    public ResponseEntity<Object> register(

//            @RequestParam String username, @RequestParam String password, //

            @RequestBody Player player //this is an alternative to @request param, as it will request all field from the constructor Player
    ) {

//        if (username.isEmpty() || password.isEmpty()) {
//            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
//        } this version can be use in case of @Requestparam, as username is properly define, below, you will see player.getUsername ...
//        ... or player.getpassword to define the same

                if (player.getUsername().isEmpty() || player.getPassword().isEmpty()) {
        return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
    }


        if (playerRepo.findByUsername(player.getUsername()) !=  null) {
        return new ResponseEntity<>("Name already in use", HttpStatus.CONFLICT);
    }

        player.setPassword(passwordEncoder.encode(player.getPassword()));

        playerRepo.save(player);
    //its important to note that as i encode the password in salvo application, it must encore while creating a new one in the front end
        return new ResponseEntity<>(HttpStatus.CREATED);
}

    private PasswordEncoder passwordEncoder() {
         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private Map<String, Object> sendInfo(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @PostMapping("/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String, Object>> creatingShips (@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody  Set<Ship> ships){

        Long connectedPlayer = getLoggedPlayer(authentication).getId();
        GamePlayer currentGP = gamePlayerRepo.getOne(gamePlayerId);
        Set<Ship> currentShip = currentGP.getShips();


        if (authentication == null) {
            return new ResponseEntity<>(sendInfo("error", "You need to be logged in to create your ships! Please Log in or Sign up."), HttpStatus.UNAUTHORIZED);
        }

        if (currentGP.getId() == null) {
            return new ResponseEntity<>(sendInfo("error", "Your GamePlayer ID have no match!"), HttpStatus.UNAUTHORIZED);
        }

        if (currentGP.getPlayer().getId() != connectedPlayer){
            return new ResponseEntity<>(sendInfo("error", "Your GamePlayer ID dont match the one of the connected player!"), HttpStatus.UNAUTHORIZED);
         }

        if (currentShip.size() >= 5) {
            return new ResponseEntity<>(sendInfo("error", "Your ships are already placed!"), HttpStatus.FORBIDDEN);
        }

        if (ships.size() != 5){
            return new ResponseEntity<>(sendInfo("error", "you are either putting too much ships or not enough!"), HttpStatus.FORBIDDEN);
            }

        else{
            for (Ship ship : ships) {
                ship.setGamePlayer(currentGP);
                shipRepo.save(ship);
            }
                return new ResponseEntity<>(sendInfo("OK", "Ship positions saved successfully!"), HttpStatus.CREATED);
        }
    }

    @PostMapping("/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> creatingSalvoes(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody  List<String> salvoes){

            //first check if the amount of salvo is the correct one

        int checkAmountSalvoes = salvoes.size();

        if(checkAmountSalvoes != 5){
            return new ResponseEntity<>(sendInfo("error", "The server cannot accept more or less than 5 salvoes!"), HttpStatus.LOCKED);
        }

        //then we check if the connected player is the good one, and if the opponent is connected

        Long connectedPlayer = getLoggedPlayer(authentication).getId();
        GamePlayer currentGP = gamePlayerRepo.getOne(gamePlayerId);
        GamePlayer opponent = getOpponent(currentGP);

        if(opponent == null){
            return new ResponseEntity<>(sendInfo("error", "You need to wait until your oppponent arrives!"), HttpStatus.LOCKED);
        }

        if (authentication == null) {
            return new ResponseEntity<>(sendInfo("error", "You need to be logged in to fire your salvoes!! Please Log in or Sign up."), HttpStatus.UNAUTHORIZED);
        }

        if(connectedPlayer != currentGP.getPlayer().getId()){
            return new ResponseEntity<>(sendInfo("error", "Your GamePlayer ID dont match the one of the connected player!"), HttpStatus.UNAUTHORIZED);
        }

        if (currentGP.getId() == null) {
            return new ResponseEntity<>(sendInfo("error", "Your GamePlayer ID have no match!"), HttpStatus.UNAUTHORIZED);
        }

        //making sure we are waiting the other player

        Integer opponentSalvoTurn = opponent.getSalvoes().size();
        Integer currentGPSalvoTurn = currentGP.getSalvoes().size();

        if (currentGPSalvoTurn > opponentSalvoTurn){
            return new ResponseEntity<>(sendInfo("error", "you need to wait until your opponent played!"), HttpStatus.FORBIDDEN);
        }

        else {

            Salvo salvo = new Salvo(currentGPSalvoTurn +1,salvoes,currentGP);
            salvoRepo.save(salvo);
            currentGP.setSalvoes(salvo);

            return new ResponseEntity<>(sendInfo("OK", "Salvoe saved successfully!"), HttpStatus.CREATED);}
    }
}

