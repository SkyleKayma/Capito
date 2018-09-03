package fr.skyle.cardgame.rest.model

import fr.skyle.cardgame.model.Game
import fr.skyle.cardgame.model.GamePlayer
import io.realm.RealmList

data class RestGame(var name: String = "", var isStarted: Boolean = false, var players: HashMap<String, RestGamePlayer?> = hashMapOf()) {

    fun toGame(idGame: String): Game {
        val game = Game(idGame, name, isStarted)

        val list = RealmList<GamePlayer>()

        for (item in players) {
            list.add(GamePlayer(item.key, item.value?.isReady ?: false, item.value?.isLeader
                    ?: false))
        }

        game.players = list

        return game
    }
}