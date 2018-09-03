package fr.skyle.cardgame.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class GamePlayer(@PrimaryKey var idPlayer: String = "",
                      var isReady: Boolean = false,
                      var isLeader: Boolean = false) : RealmObject() {

    fun toHashMap(): HashMap<String, Any?> {
        val hashMapGamePlayerInfo = hashMapOf<String, Any?>()
        hashMapGamePlayerInfo.apply {
            put(GamePlayer::isReady.name, isReady)
            put(GamePlayer::isLeader.name, isLeader)
        }

        return hashMapGamePlayerInfo
    }
}