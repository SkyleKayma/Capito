package fr.skyle.capito.model

class Game(var idGame: String? = "",
           var name: String? = "",
           var started: Boolean? = false,
           var players: HashMap<String, GamePlayer>? = hashMapOf())