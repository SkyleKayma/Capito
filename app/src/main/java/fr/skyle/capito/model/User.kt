package fr.skyle.capito.model

class User(var pseudo: String? = "",
           var nbGamesWin: Int? = 0,
           var nbGamesLose: Int? = 0,
           var currentGame: String? = null)