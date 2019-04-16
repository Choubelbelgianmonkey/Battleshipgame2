let salvo = new Vue({
    el: "#app",
    
    mounted() {
        
        this.getUrlVars(), 
        this.getData(),
        this.populateTable("table1"),
        this.populateTable("table2")
        
    },
        
    data: {
        
        //those are for security check (making sure the proper player access the data)
        
        gamePlayerURL: [],
        connectedPlayer: [],
        connectedUsername: [],
        
        //those are for creating the page
        gpData: [],
        gpShips: [],
        shipsName: [],
        currentGP: [],
        mySalvoes: [],
        hisSalvoes: [],
        search: [],
        
    },

    methods: {
        
        getUrlVars() {
            
                        var vars = {};
                        var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
                        vars = value;
                    
//                    vars[key] = value; this prevent us to have directly the number of game player, instead we have GP=1
                                        });

            
                    this.gamePlayerURL = vars;
},
        
        getData() {
            
            let url = "/api/game_view/" + this.gamePlayerURL;
            
             const urlGames = "/api/games2";
            
            //the first fecth provide me the data from the "gamplayer"API, which will afterward be used to populate the table
            //... and the ships and salvo withins
            
            Promise.all([
                
            fetch(url, {
                mode: "cors"
            })
            .then(function(response){
                return response.json()
            })
            .then(function(gameJson){
                
                
                if(gameJson.hasOwnProperty("error")){
                      alert(gameJson.error)
                
                history.go(-1);
                }
                
                salvo.gpData = gameJson;
                salvo.gpShips = gameJson.game.ships
                salvo.currentGP = gameJson.game.gamePlayer
                salvo.mySalvoes = gameJson.game.mySalvoes
                salvo.hisSalvoes = gameJson.game.hisSalvoes
               
                
                salvo.colorizeGrid(); 
                salvo.playersVs();
                    
            })
            .catch(error => console.log(error)),
                
                //the second fetch will make sure the data can be accessed, hence will get and isolate the username of the 
                //... current player, afterward a function will be use to make sure this page is accessed by the proper person
                
             setTimeout(function(){ fetch(urlGames, {
                mode: "cors"
            })
            .then(function(response){
                return response.json()
            })
            .then(function(response){
                salvo.connectedPlayer = response.player
            })
            .then(function(response){
                    salvo.connectedUsername  = salvo.connectedPlayer.username; 
            })
            .catch(error => console.log(error)); }, 10)
            ]);   
        },
 
        populateTable(tableHTML) {
            
    const column = ["","A","B","C","D","E","F","G","H","I","J"];
    const see = document.getElementById(tableHTML);
    const table = document.createElement("table");
    const tbody = document.createElement("tbody");
            
            //the element above will be use later
            
    table.appendChild(tbody);
            
            //bind the tbody  to the table element, the tbody will be then build with the 2 loops below

    for(var i = 0; i<11 ; i++){

        var tr = document.createElement("tr");
        tbody.appendChild(tr);
        
        //bind the tr element to the tbody, the tr will be built below: 
        
        for(var j = 0; j<11 ; j++){
            
            var td = document.createElement("td");
            
            if (i == 0 && j>0){
                td.textContent = j ;
            } //this will add the content of the first row(the numbers)
            
            if (i > 0 && j == 0){
                td.textContent = column[i] ;
           } //this will add the content of the first columnthe letter)
            
            if (i > 0 && j > 0){
                
                            td.setAttribute("id",column[i] + j + tableHTML);
             //this will add an ID to match each cell (eg: cell A1 will have the ID="A1" )
                
            }
            
            tr.appendChild(td);
            //this bind the builded cells to the rows, whichare themselves built at the first loop
            
        }
    }          
    see.appendChild(table);       
            //td bind to  tr
            //tr bind to tbody & thead
            //tbody bind to table
            // and now, we are pushing the table to the body , which is the ID of the div in HTMl page
              
},
           
        colorizeGrid() {

            let myShips =  this.gpShips;
            let myShipsLocation = [];
            
            let hisSalvoes = this.hisSalvoes;
            let mySalvoes = this.mySalvoes;
            
            //this loops added My ships in the table1 and isolate their location in a variable to be compared 
            // the salvo of the opponents
            for(let i = 0 ; i < myShips.length ; i++){
                
                this.shipsName.push(myShips[i].type);
                
                let myShipLocation = myShips[i].locations;
                
                let frontShip = myShipLocation[0];
                let backShip = myShipLocation[myShipLocation.length - 1 ];
                       
                    for (let j = 0 ; j < myShipLocation.length ; j++){    

                        document.getElementById(myShipLocation[j]+"table1").style.backgroundColor='#d9e033';
                        
                            myShipsLocation.push(myShipLocation[j])
                        }   
                }   
            
            //this loops added the salvo of the oppponent and set up color if they hit or miss, accordingly with
            // the array set up on the previous loop
            // added if statement in case no salvoes has been shot
            if(hisSalvoes !== undefined){
            for(let k = 0 ; k < hisSalvoes.length; k++){

                let hisSalvolocation = hisSalvoes[k].location;
               

                    for(l = 0; l < hisSalvolocation.length; l++){
                        
                        if(myShipsLocation.includes(hisSalvolocation[l])){
                            
                        
                             document.getElementById(hisSalvolocation[l]+"table1").textContent = hisSalvoes[k].turn

                            
                            document.getElementById(hisSalvolocation[l]+"table1").style.backgroundColor="#d00909" 
                        }
                        else{document.getElementById(hisSalvolocation[l]+"table1").style.backgroundColor="#007d87"
                                document.getElementById(hisSalvolocation[l]+"table1").textContent = hisSalvoes[k].turn
                            } 
                    }
             }}

            //this loop add my salvoes in the table2
            //added if statement if no salvoes has been fired
            if(mySalvoes !== undefined){
            for(let m = 0; m < mySalvoes.length; m++){
                
                let hisSalvoLocation = mySalvoes[m].location;
                
                    for(let n = 0; n < hisSalvoLocation.length; n++){
                        
                        document.getElementById(hisSalvoLocation[n]+"table2").style.backgroundColor="#007d87"
                        
                        document.getElementById(hisSalvoLocation[n]+"table2").textContent = mySalvoes[m].turn
                    }
                } }
            
            },

        playersVs(){
            
            let players = document.getElementById("gamers")

            if (this.gpData.game.gamePlayer.length == 1) {

                let gamer1 = this.currentGP[0].player.username;

                players.textContent = gamer1 + "(you) VS awaiting his opponent ";

            } else {

                let gamer1 = this.currentGP[0].player.username;
                let gamer2 = this.currentGP[1].player.username;
                
                if (this.gamePlayer == this.currentGP[0].id)
                    {
                    players.textContent = gamer1 + " (you) " + "VS" + " " + gamer2;
                } else {
                    players.textContent = gamer2 + " (you)" + "VS" + " " + gamer1;
                }

            }

        }
            
    },
})

