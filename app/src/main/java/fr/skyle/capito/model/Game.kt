package fr.skyle.capito.model

class Game(
    var idGame: String? = null,
    var name: String? = null,
    var started: Boolean? = false,
    var players: HashMap<String, GamePlayer>? = hashMapOf()
)