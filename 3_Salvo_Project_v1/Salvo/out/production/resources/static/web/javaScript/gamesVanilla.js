var data;
var games;
var gamePlayers;
var username = [];



fetch("/api/games",{
    method:"GET",

}).then(function(response){
    if (response.ok){

        return response.json();
    }

}).then(function(json){
    data = json;

    gameList();

}).catch(function (error){
    console.log("Request failed:" + error.message);
});


function gameList(){

    var letsGetStarted = document.getElementById("ol");

    for (var i = 0; i < data.length; i++) {

        games = data[i].date;
        gamePlayers = data[i].gamePlayer;

           var list = document.createElement("li");

         for (var j = 0; j < gamePlayers.length; j++){

            username.push(" " + gamePlayers[j].player.userName)

        }

        list.textContent =  games + ": " + username.splice(0,2);
        // The textContent property sets or returns the text content of the specified node, and all its descendants.
        // cf: https://www.w3schools.com/jsref/prop_node_textcontent.asp

    

        letsGetStarted.appendChild(list);

    }

}


