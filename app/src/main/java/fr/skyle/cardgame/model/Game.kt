package fr.skyle.cardgame.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Game(@PrimaryKey var idGame: String = "",
                var name: String = "",
                var isStarted: Boolean = false,
                var players: RealmList<GamePlayer> = RealmList()) : RealmObject() {

    fun toHashMap(): HashMap<String, Any?> {
        val hashMap = hashMapOf<String, Any?>()
        hashMap.apply {
            put(Game::name.name, name)
            put(Game::isStarted.name, isStarted)
        }

        val map = hashMapOf<String, Map<String, Any?>>()

        for (player in players) {
            map[player.idPlayer] = player.toHashMap()
        }

        hashMap[Game::players.name] = map

        return hashMap
    }
}