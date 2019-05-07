let salvo = new Vue({
    el: "#app",

    mounted() {
            this.getUrlVars(),
            this.getData(),
            this.populateTable("tableA"),
            this.populateTable("table2"),
            this.sendShips()
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

        //drag and drop variables:

        draggingItem: [],
        ships: [],
        allIDs: [],
        boatVertical: true,
        currentMovingBoat: [],
        newAllIDs: [],
        witness: 0,

        occurence: 0,
        hasBeenDone: false,
        
    },

    methods: {
        getUrlVars() {

            var vars = {};
            var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
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
                    .then(function (response) {
                        return response.json()
                    })
                    .then(function (gameJson) {


                        if (gameJson.hasOwnProperty("error")) {
                            alert(gameJson.error)

                            history.go(-1);
                        }

                        // console.log(gameJson)

                        salvo.gpData = gameJson;
                        salvo.gpShips = gameJson.game.ships
                        salvo.currentGP = gameJson.game.gamePlayer
                        salvo.mySalvoes = gameJson.game.mySalvoes
                        salvo.hisSalvoes = gameJson.game.hisSalvoes


                        salvo.colorizeGrid();


                    })
                    .catch(error => console.log(error)),

                //the second fetch will make sure the data can be accessed, hence will get and isolate the username of the 
                //... current player, afterward a function will be use to make sure this page is accessed by the proper person

                setTimeout(function () {
                    fetch(urlGames, {
                        mode: "cors"
                    })
                        .then(function (response) {
                            return response.json()
                        })
                        .then(function (response) {
                            salvo.connectedPlayer = response.player
                        })
                        .then(function (response) {
                            salvo.connectedUsername = salvo.connectedPlayer.username;
                            salvo.playersVs();
                        })
                        .catch(error => console.log(error));
                }, 10)
            ]);
        },
        populateTable(tableHTML) {

            const column = ["", "", "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "", ""]
            const see = document.getElementById(tableHTML);
            const table = document.createElement("table");
            const tbody = document.createElement("tbody");

            //the element above will be use later

            table.appendChild(tbody);

            //bind the tbody  to the table element, the tbody will be then build with the 2 loops below

            for (var i = 0; i < 15; i++) {

                var tr = document.createElement("tr");
                tbody.appendChild(tr);

                //bind the tr element to the tbody, the tr will be built below: 

                for (var j = 0; j < 15; j++) {

                    var td = document.createElement("td");

                    if (i == 2 && j > 2 && j < 13) {
                        td.textContent = j - 2;
                        td.setAttribute("id", column[i] + j + tableHTML);
                    } //this will add the content of the first row(the numbers)

                    if (i > 2 && j == 2) {
                        k = j-2
                        td.textContent = column[i];
                        td.setAttribute("id", column[i] + k + tableHTML);
                    } //this will add the content of the first columnthe letter)

                   if (i > 2 && j > 2 && i < 13 && j < 13)  {

                    k = j-2

                        td.setAttribute("id", column[i] + k + tableHTML);

                        //this will add an ID to match each cell (eg: cell A1 will have the ID="A1" )
                        td.setAttribute("class", "empty")
                    }

                    tr.appendChild(td);
                    //this bind the builded cells to the rows, whichare themselves built at the first loop

                 if (i == 1 || i == 13){
                     td.setAttribute("class", "outbound")
                 }
                if(j == 1 || j == 13){
                    td.setAttribute("class", "outbound")

                } //this in order to prevent ship to go outside the grid

                
                if (i == 0 || i == 14){
                    td.setAttribute("class", "outbound2")
                }

               if(j == 14 || j == 0){
                   td.setAttribute("class", "outbound2")

                     } //this in order to prevent ship to go outside the grid
                }
            }

            see.appendChild(table);
          document.addEventListener("drop", this.dragDrop)
            //td bind to  tr
            //tr bind to tbody & thead
            //tbody bind to table
            // and now, we are pushing the table to the body , which is the ID of the div in HTMl page

        },
        colorizeGrid() {

            let myShips = this.gpShips;
            let myShipsLocation = [];

            let hisSalvoes = this.hisSalvoes;
            let mySalvoes = this.mySalvoes;

            //this loops added My ships in the tableA and isolate their location in a variable to be compared 
            // the salvo of the opponents
            for (let i = 0; i < myShips.length; i++) {
                let locs = myShips[i].locations;
                let types = myShips[i].type;

                let frontShip = locs[0];
                let backShip = locs[locs.length - 1];


                for (let j = 0; j < locs.length; j++) {

                    document.getElementById(locs[0] + "tableA").classList.add("switchOrientation")
    
                  
                    document.getElementById(locs[j] + "tableA").classList.add("shipColor");
                    document.getElementById(locs[j] + "tableA").classList.add(types);
                    document.getElementById(locs[j] + "tableA").setAttribute('data-shipLength', locs.length);
                    
                    document.getElementById(locs[j] + "tableA").setAttribute('data-shipType', types);
                    if (j == 0) {
                        document.getElementById(locs[j] + "tableA").setAttribute("draggable", "true");
                    }
                    if (document.getElementById(locs[j] + "tableA").classList.contains("shipColor")) {
                        document.getElementById(locs[j] + "tableA").classList.remove("empty");
                    }
                    let empties = document.getElementsByClassName("empty");
                    let filled = document.getElementsByClassName("shipColor");
                    for (let empty of empties) {
                        empty.addEventListener("dragover", this.dragOver);
                        empty.addEventListener("dragenter", this.dragEnter);
                        empty.addEventListener("dragleave", this.dragLeave);
                        empty.addEventListener("drop", this.dragDrop);
                    }
                    for (let fill of filled) {
                        fill.addEventListener("dragstart", this.dragStart);
                        fill.addEventListener("dragend", this.dragEnd);
                    }
                }
            }

            //this loops added the salvo of the oppponent and set up color if they hit or miss, accordingly with
            // the array set up on the previous loop
            // added if statement in case no salvoes has been shot
            if (hisSalvoes !== undefined) {
                for (let k = 0; k < hisSalvoes.length; k++) {

                    let hisSalvolocation = hisSalvoes[k].location;

                    for (l = 0; l < hisSalvolocation.length; l++) {

                        if (myShipsLocation.includes(hisSalvolocation[l])) {

                            document.getElementById(hisSalvolocation[l] + "tableA").textContent = hisSalvoes[k].turn

                            document.getElementById(hisSalvolocation[l] + "tableA").style.backgroundColor = "#d00909"
                        } else {
                            document.getElementById(hisSalvolocation[l] + "tableA").style.backgroundColor = "#007d87"
                            document.getElementById(hisSalvolocation[l] + "tableA").textContent = hisSalvoes[k].turn
                        }
                    }
                }
            }

            //this loop add my salvoes in the table2
            //added if statement if no salvoes has been fired
            if (mySalvoes !== undefined) {
                for (let m = 0; m < mySalvoes.length; m++) {

                    let hisSalvoLocation = mySalvoes[m].location;

                    for (let n = 0; n < hisSalvoLocation.length; n++) {

                        document.getElementById(hisSalvoLocation[n] + "table2").style.backgroundColor = "#007d87"

                        document.getElementById(hisSalvoLocation[n] + "table2").textContent = mySalvoes[m].turn
                    }
                }
            }

            this.addSwitchOrientation()

        },
        playersVs() {

            let players = document.getElementById("gamers")

            if (this.gpData.game.gamePlayer.length == 1) {

                let gamer1 = this.currentGP[0].player.username;

                players.textContent = gamer1 + "(you) VS awaiting his opponent ";

            } else {

                let gamer1 = this.currentGP[0].player.username;
                let gamer2 = this.currentGP[1].player.username;

                let connectedPlayer = this.connectedUsername

                if (gamer1 == connectedPlayer) {
                    players.textContent = gamer1 + " (you) " + "VS" + " " + gamer2;
                } else {
                    players.textContent = gamer2 + " (you)" + "VS" + " " + gamer1;
                }

            }

        },
        sendShips() {


            let url = "/api/games/players/" + this.gamePlayerURL + "/ships";
            let myInit = {

                method: 'POST',
                mode: 'cors',
                cache: 'default',
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify([
                    { "type": "Carrier", "location": ["A5", "B5", "C5", "D5", "E5"] },
                    { "type": "Submarine", "location": ["D1", "E1", "F1"] },
                    { "type": "Battleship", "location": ["G3", "G4", "G5", "G6"] },
                    { "type": "Destroyer", "location": ["C8", "C9", "C10"] },
                    { "type": "Patrol_Boat", "location": ["I5", "I6"] }
                ])

            };

            fetch(url, myInit)
                .then(function (response) {
                    return response.json()
                })
                .then(function (gameJson) {
                    if (!window.location.hash) {
                        window.location = window.location + '#loaded';
                        window.location.reload();
                    }
                })
                .catch(function (error) {

                    //  console.log("Request failed: " + error.message);
                    // console.log(error)
                });
        },
        showShips() {

           // console.log("ici");

            let ships = tableA.shipLocs;
            for (let i = 0; i < ships.length; i++) {
                let locs = ships[i].location;
                let types = ships[i].type;


                for (let j = 0; j < locs.length; j++) {

                   
                    //                    document.getElementById(locs[j]).style.backgroundColor = "red";
                    document.getElementById(locs[j]).classList.add("shipColor");
                    document.getElementById(locs[j]).setAttribute('data-shipLength', locs.length);
                    document.getElementById(locs[j]).setAttribute('data-shipType', types);
                    if (j == 0) {
                        document.getElementById(locs[j]).setAttribute("draggable", "true");
                    }
                    if (document.getElementById(locs[j]).classList.contains("shipColor")) {
                        document.getElementById(locs[j]).classList.remove("empty");
                    }
                    let empties = document.getElementsByClassName("empty");
                    let filled = document.getElementsByClassName("shipColor");
                    for (let empty of empties) {
                        empty.addEventListener("dragover", this.dragOver);
                        empty.addEventListener("dragenter", this.dragEnter);
                        empty.addEventListener("dragleave", this.dragLeave);
                        empty.addEventListener("drop", this.dragDrop);
                    }
                    for (let fill of filled) {
                        fill.addEventListener("dragstart", this.dragStart);
                        fill.addEventListener("dragend", this.dragEnd);
                    }
                }
            }
        },
        dragStart(e) {
         //to prevent the alter "out of grib" to be triggered several times, these need to be reset
         //... cf dragEnter for more info
            this.occurence = 0;
            this.hasBeenDone = false

            // the following is to avoid conflict between "1" and "10"

            let letter = e.target.id.substr(0, 1);

            let test1 = (e.target.id).split("");
            let test2 = (letter+"tableA").split("");

                        for (let x =0; x < test1.length; x++) {
                           for (let c = 0; c < test2.length; c++){
                                if (test1[x] == test2[c]){
                                    test1.splice(x, 1); 
                                       }
                                  }
                            }

            let number = test1.join("");
          
        

            this.shipLength = document.getElementById(e.target.id).getAttribute("data-shipLength");
            this.shipType = document.getElementById(e.target.id).getAttribute("data-shipType");

            this.currentMovingBoat = document.getElementById(e.target.id).getAttribute("data-shipType");

            //  console.log(e.target.id)
            //  console.log(letter)
            //  console.log(test1)
            //  console.log(test2)
            //  console.log(number)

            //check if vertical or horizontal
            let boatByType = [];
            let stupidDomEl = document.getElementsByClassName(this.shipType);

            for (let k = 0; k < stupidDomEl.length; k++) {
                boatByType.push(stupidDomEl[k].id);
            }
            for (let l = 0; l < boatByType.length - 1; l++) {
                if (boatByType[l][0] != boatByType[l + 1][0]) {
                    salvo.boatVertical = true;
                } else {
                    salvo.boatVertical = false;
                }
            }

            //count the size of the ship:

            salvo.allIDs = [];


            if (this.boatVertical == true) {

                for (let i = 0; i < this.shipLength; i++) {

                    let newID = salvo.incrementingLetter(letter, i) + number;


                    salvo.allIDs.push(newID);
                }

            } else {

                for (let i = 0; i < this.shipLength; i++) {

                    let newID = letter + (Number(number) + i);

                    salvo.allIDs.push(newID);
                }
            }


            // for (let i = 0; i < this.shipLength; i++) {
            //     let newID = letter + (Number(number) + i);
            //     salvo.allIDs.push(newID)

            // }

            for (let i = 0; i < salvo.allIDs.length; i++) {

                const bob = document.getElementById(salvo.allIDs[i] + "tableA");
                const bob2 = document.getElementById(salvo.allIDs[0] + "tableA")

                bob2.classList.remove("switchOrientation")
                bob2.removeEventListener('click', this.switchOrientation)
                bob2.innerHTML = "";
                
                
                bob2.removeAttribute("draggable"); 

                bob.removeAttribute("class");  
                bob.removeAttribute("data-shipLength"); 
                bob.removeAttribute("data-shipType");
                bob.classList.add("empty");   
            }
        },
        dragEnd(e) {
   // console.log("END", e.target.id);


        },
        dragOver(e) {


   
            // console.log("OVER", e.target.getAttribute("class"))


    salvo.fullCell = false;
    //dragOver is necessary otherwise the ship goes back to its original place
    e.preventDefault();

    let id = e.target;
    let shipCellID = e.target.id;
    // console.log(shipCellID)
    let letter = shipCellID.substr(0, 1);
    let number = shipCellID.substr(1, 1);
    let allIDsOver = []
    for (let i = 0; i < this.shipLength; i++) {
        let newID = letter + (Number(number) + i)
        //                console.log(newID)
        allIDsOver.push(newID)
    }

    for (let i = 0; i < allIDsOver.length; i++) {
        if(document.getElementById(allIDsOver[i] + "tableA") == null){}
            else if (document.getElementById(allIDsOver[i] + "tableA").classList.contains("notAllowed")) {
                 salvo.fullCell = true;
              }
    }
    // console.log(allIDsOver)
        },
        dragEnter(e) {

         //  console.log("ENTER", e.target.id)
        // console.log("ENTER", e.target.classList)
           
            let outbound = e.target.getAttribute("class");
            

        if (outbound == "outbound"){

                if(this.hasBeenDone == false && this.occurence < 1){

                    //console.log("if")
                    this.refillGrid(this.allIDs);
                    alert("dont go outside the grid!")

                this.occurence++;
                this.hasBeenDone = true
                }}

        
//if(this.hasBeenDone == true && this.occurence > 1)

        e.preventDefault();

    let id = e.target;
    let shipCellID = e.target.id;

    let letter = shipCellID.substr(0, 1);
    let number = shipCellID.substr(1, 1);
    let allIDsEnter = []

    for (let i = 0; i < this.shipLength; i++) {
        let newID = letter + (Number(number) + i)
        allIDsEnter.push(newID)
    }

    for (let i = 0; i < allIDsEnter.length; i++) {
        if(document.getElementById(allIDsEnter[i] + "tableA") == null){}


       else if (document.getElementById(allIDsEnter[i] + "tableA").classList.contains("shipColor")) {
            salvo.fullCell = true;
            document.getElementById(allIDsEnter[i] + "tableA").classList.add("notAllowed")
        }
    }

    
        },
        dragLeave(e) {

            // console.log("LEAVE", e.target.getAttribute("class"))



    let letter = e.target.id.substr(0, 1);
    let number = e.target.id.substr(1, 1);
    //remove the ships here
    let allIDsLeave = []
    for (let i = 0; i < this.shipLength; i++) {
        let newID = letter + (Number(number) + i);
        allIDsLeave.push(newID)
    }

    for (let i = 0; i < allIDsLeave.length; i++) {

        let bob = document.getElementById(allIDsLeave[i] + "tableA");

        if(bob != null){

        if (!bob.classList.contains("notAllowed") 
            && !bob.classList.contains("shipColor")) {
            bob.classList.remove("shipColor");
            bob.removeAttribute("draggable");
            bob.removeAttribute("data-shiplen gth");
            bob.removeAttribute("data-shiptyp e");
            bob.classList.add("empty"); 

        }
        if (bob.classList.contains("notAllowed")) {
            bob.classList.remove("notAllowed")
        }
        }
    }
    // }
        },
        dragDrop(e) {

               // console.log("DROP", e.target.getAttribute("class"))

    let shipCellID = e.target.id;
            
    let types = salvo.gpShips;
    let letter = shipCellID.substr(0, 1);


//this allow to use 10 as a number : 
    let number = shipCellID.substr(1, 1);
    let numberbis = shipCellID.split("");
    let numbertris = (letter+"tableA").split("");

    for (let x =0; x < numberbis.length; x++) {
        for (let c = 0; c < numbertris.length; c++){
            if (numberbis[x] == numbertris[c]){
                numberbis.splice(x, 1); 
            }
        }
    }
    let numberquis = numberbis.join("")

    if (this.boatVertical == true) {

        for (let i = 0; i < this.shipLength; i++) {

            let newID = salvo.incrementingLetter(letter, i) + numberquis;

            salvo.newAllIDs.push(newID)
        }

    } else {

        for (let i = 0; i < this.shipLength; i++) {

            let newID = letter + (Number(numberquis) + i)

            salvo.newAllIDs.push(newID)
        }
    }

    //here we prevent the ship to collide inside the grid

        let locsOutGrid = [];
        let allCellsClear =  [];
    
        for (let a = 0; a < salvo.newAllIDs.length; a++) {
                
            locsOutGrid.push(document.getElementById(salvo.newAllIDs[a] + "tableA")); 

            } 
            if(!locsOutGrid.includes(null)) {

            for (let m = 0; m < locsOutGrid.length; m++){

                    if(locsOutGrid[m].classList.contains("empty") == false)
                    {  allCellsClear.push(false)}
                    else{
                        allCellsClear.push(true)
                    }
            }
        }

         // console.log(locsOutGrid)


        if (locsOutGrid.includes(null) || allCellsClear.includes(false)) {

           //  console.log("1")
            salvo.refillGrid(salvo.allIDs)

        } else {
           //  console.log("2")
            salvo.refillGrid(salvo.newAllIDs);
        }
    

    for (let y = 0; y < types.length; y++) {
        if (this.shipType == types[y].type) {
            types[y].location = salvo.allIDs
        }
    }
        
            salvo.newAllIDs = [];
           
        },
        incrementingLetter(letter, occurence){

    let alphabet = ("ABCDEFGHIJ").split("");

    for (let i = 0; i < alphabet.length; i++) {

        if (letter == alphabet[i]) {

            let k = i + occurence;

            return alphabet[k];

        }
    }
        },
        refillGrid(shipIDs){

        for (let i = 0; i < shipIDs.length; i++) {

        let bob = document.getElementById(shipIDs[i] + "tableA");
        let bob2 = document.getElementById(salvo.allIDs[i] + "tableA");

        // if (bob.classList.contains("empty")) {

            document.getElementById(shipIDs[0] + "tableA").setAttribute("draggable", "true");
            document.getElementById(shipIDs[0] + "tableA").addEventListener("dragstart", this.dragStart);
            document.getElementById(shipIDs[0] + "tableA").classList.add("switchOrientation")

            bob.classList.add("shipColor")
            bob.classList.add(this.currentMovingBoat);
            bob.classList.remove("empty");
            bob.setAttribute("data-shipLength", shipIDs.length);
            bob.setAttribute('data-shipType', this.currentMovingBoat);

            salvo.allIDs = [];
            salvo.newAllIDs = [];

            this.addSwitchOrientation();

             }
        },
        addSwitchOrientation(){
            
           let domForButton = document.getElementsByClassName("switchOrientation");

           //console.log(domForButton)
           
           for (let i = 0; i < domForButton.length; i++){
            domForButton[i].innerHTML = "<img src=./images/switchOrientation.png width=\'10px\' height=\'10px\' draggable=´false´>";
            domForButton[i].addEventListener('click', this.switchOrientation)
           }
           

        },
        switchOrientation(){

            let boatByType = [];
            let stupidDomEl = document.getElementsByClassName(this.shipType);

            for (let k = 0; k < stupidDomEl.length; k++) {
                boatByType.push(stupidDomEl[k].id);
            }
            for (let l = 0; l < boatByType.length - 1; l++) {
                if (boatByType[l][0] != boatByType[l + 1][0]) {
                    salvo.boatVertical = true;
                } else {
                    salvo.boatVertical = false;
                }
            }


            console.log(e.target.id)
           alert("i am switching")


        }
    }
})