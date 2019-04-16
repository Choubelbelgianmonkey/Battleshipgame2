let salvo = new Vue({
    el: "#app",
    mounted() {
        this.getData()
    },
    data: {
        games : [],
    },
    methods: {
        getData() {
            let url = "http://localhost:8080/api/games";

            fetch(url, {
                mode: "cors"
            })
            .then(function(response){
                return response.json()
            })
            .then(function(gameJson){
                salvo.games = gameJson
            })
            .catch(error => console.log(error))
        }
    }
})