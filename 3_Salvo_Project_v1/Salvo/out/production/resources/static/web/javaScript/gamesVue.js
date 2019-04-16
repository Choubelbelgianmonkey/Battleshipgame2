let salvo = new Vue({
    el: "#app",
    created() {

    },

    mounted() {
        this.getData()
    },

    updated() {


    },

    data: {
        
        gamesData: [],
        currentPlayer: [],
        
        scoresData: [],
       
        
        urlGames: "/api/games",
        urlScores: "/api/leaderboard",
        
        //postdata 
        username: "",
        password: "",
        lastname: "",
        firstname: "",
        
    },
    
    methods: {
        
        getData() {

            var urlGames = "/api/games2";
            var urlScores = "/api/leaderboard";

            Promise.all([


        fetch(urlGames).then((response) => response.json())
                        .then((response) => salvo.gamesData = response.games),
                
        fetch(urlGames).then((response) => response.json())
                     .then((response) =>  salvo.currentPlayer = response.player),  
                
        fetch(urlScores).then((response) => response.json())
                        .then((response) => salvo.scoresData = response.score)
                        .catch(function (error) { alert('Error: ' + error.message)})
        ]).
            
            then(this.leaderBoard)
        },
        
        validateEmail() {
            
            let email = this.username
            
  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            
  return re.test(email);
},
        
        subscribe(){
            
            if(this.validateEmail() == false){alert("please add an emaill address as username")}
                else{ // making sure an email address is used as username
            
            Promise.all([
                
                    fetch("/api/players", {
                    credentials: 'include', //keeps you logged in
                    headers: {
                        'Content-Type': 'application/json' ///this will save the data in the backend
                    },
                    method: 'POST',
                    body: JSON.stringify({ //unlike login it must be send as json, throug this line of code
                        username: this.username,
                        password: this.password,
                        firstName: this.firstname,
                        lastName: this.lastname
                    })
                }).then(this.login)])}

        },
        
        leaderBoard() {

            //those elements below will be use to create the table

            const scores = this.scoresData;
            const column = ["Name", "Won", "Lost", "Tied", "Game Played", "Total"]; // check "if" statement below in case of update this array
                  
            const leaderBoard = document.getElementById("leaderBoard");
            const table = document.createElement("table");
            const tbody = document.createElement("tbody");
            
            const leaderBoardHeader = document.getElementById("leaderBoardHeader");
            const thead = document.createElement("thead");
            
            //this loop will sort the json, to ensure that is the same total occur, its the player with less game who will take the lead
            
                
            table.appendChild(thead);
           
            for(var k = 0; k < 1; k++) {
                
                var tr = document.createElement("tr");
                
                thead.appendChild(tr);
                
                    for (let m = 0; m < column.length; m++){
                
                            const th = document.createElement("th");
                        
                            th.textContent = column[m]
                        
                            tr.appendChild(th)
                
                            
    
                }
            }
            
            
            table.appendChild(tbody);
            
            
            

            for (var i = 0; i < scores.length; i++) {

                let arrayScore = scores[i];
                let username = scores[i].username;
                let score = scores[i].score;
                let amountOfGames = scores[i].score.length;
                
                
                var tr = document.createElement("tr");

                tbody.appendChild(tr);

                //bind the tr element to the tbody, the tr will be built below: 

                for (var j = 0; j < column.length; j++) {

                    var td = document.createElement("td");

                    // the content of the tables are added below
                    
                    if (j == 0) {

                        td.textContent = username;
                    }

                    if (j == 1) {
                        let win = 0;

                        for (let k = 0; k < score.length; k++) {

                            let oneScore = score[k]

                            if (oneScore == 1) {
                                win++
                            }

                        }
                        td.textContent = win;

                    }

                    if (j == 2) {

                        let lose = 0;

                        for (let k = 0; k < score.length; k++) {

                            let oneScore = score[k]

                            if (oneScore == 0) {
                                lose++
                            }

                        }

                        td.textContent = lose;


                    }

                    if (j == 3) {

                        let Tied = 0;

                        for (let k = 0; k < score.length; k++) {

                            let oneScore = score[k]

                            if (oneScore == 0.5) {
                                Tied++
                            }
                        }
                        td.textContent = Tied;
                    }
                    
                    if (j == 4) {
                        
                        td.textContent = amountOfGames
                    }

                    if (j == 5) {

                        let total = 0;

                        for (let k = 0; k < score.length; k++) {

                            total += score[k]
                        }


                        td.textContent = total;

                    }

                    tr.appendChild(td);
                    //this bind the builded cells to the rows, whichare themselves built at the first loop
                }
            
                
            }
            leaderBoard.appendChild(table);

        },
        
        getBody(user) {
            var body = [];
            for (var key in user) {
                var encKey = encodeURIComponent(key);
                var encVal = encodeURIComponent(user[key]);
                body.push(encKey + "=" + encVal);
            }
            return body.join("&");
        },
        
        login() {
            fetch("/api/login", {
                    credentials: 'include', //keeps you logged in
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    method: 'POST',
                    body: this.getBody({
                        username: this.username,
                        password: this.password
                    })
                })
                .then(function (data) {
                    console.log("ALL good!")
                    location.reload(true)
                })
                .catch(function (error) {
                    console.log('Request failure: ', error);
                });
        },
        
        logout() {
            fetch("/api/logout", {
                    method: "POST",
                })
                .then(function (gameJson) {
                    console.log("Log out success")
                })
                .then(function () {
                    location.reload(true)
                })
                .catch(error => console.log(error))
        },
        
        //those two functions works togethers START

        getMatchingGameplayer(gamePlayers) {          
            
            for (let i = 0; i < gamePlayers.length; i++){
                
                if (gamePlayers[i].player.username == this.currentPlayer.username){
                    return this.joinGame(gamePlayers[i].id) 
                }
            }
        },
        
        joinGame(GP_ID){
            
            window.location.href = '/web/game.html?gp=' + GP_ID;
            
        },
        
          //those two functions works togethers START
        
        checkPlayerInGame(gamePlayers){
               
            for (let i = 0; i < gamePlayers.length; i++ ){
                
                if (gamePlayers[i].player.username == this.currentPlayer.username){
                    return true
                }
            }
        },
        
        createNewGame(){
            
            
            
            
                   var myInit = { method: 'POST',
                               mode: 'cors',
                               cache: 'default' };         

                fetch( "/api/games2", myInit)
                    
                    .then(function(response) {
                        if (response.ok) {return response.json();}})
                    .then(function(json) {                     
                    
                    var GameplayerID = json.gpid
                        
                    window.location.href = "/web/game.html?gp="+ GameplayerID
                
                
                
                })
                    .catch(function(error) {
                  console.log( "Request failed: " + error.message );
                });
            
            
        
        },
        
        joingame(gameID){
            
                        
                   var myInit = { method: 'POST',
                               mode: 'cors',
                               cache: 'default' };         

                fetch("/api/game/"+gameID+"/players", myInit)
                    
                    .then(function(response) {
                        if (response.ok) {return response.json();}})
                    .then(function(json) {                     
                    
                        console.log(json)
                    
                   if(json.hasOwnProperty("error")){
                      alert(json.error)  }
                

                })
                    .catch(function(error) {
                    
                  console.log( "Request failed: " + error.message );
                        
                             
                   if(json.hasOwnProperty("error")){
                      alert(json.error)  }

                });
            
            
        }
        
    }
    
})




