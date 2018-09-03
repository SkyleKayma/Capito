package fr.skyle.cardgame.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Player(@PrimaryKey var pseudo: String = "",
                  var nbGamesWin: Int = 0,
                  var nbGamesLose: Int = 0) : RealmObject() {

    fun toHashMap(): HashMap<String, Any?> {
        val hashMap = hashMapOf<String, Any?>()
        hashMap.apply {
            put(Player::pseudo.name, pseudo)
            put(Player::nbGamesWin.name, nbGamesWin)
            put(Player::nbGamesLose.name, nbGamesLose)
        }
        return hashMap
    }
}