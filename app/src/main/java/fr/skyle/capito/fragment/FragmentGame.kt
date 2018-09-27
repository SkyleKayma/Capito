package fr.skyle.capito.fragment

import android.os.Bundle
import com.google.firebase.database.ValueEventListener
import fr.openium.kotlintools.ext.gone
import fr.openium.kotlintools.ext.show
import fr.skyle.capito.KEY_GAME_ID
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_GAMES
import fr.skyle.capito.TABLE_PLAYERS
import fr.skyle.capito.model.Game
import fr.skyle.capito.model.GamePlayer
import kotlinx.android.synthetic.main.fragment_game.*
import timber.log.Timber

class FragmentGame : AbstractFragmentFirebase() {

    companion object {
        const val POSITION_BOT = 1
        const val POSITION_LEFT = 2
        const val POSITION_RIGHT = 3
        const val POSITION_TOP = 0
    }

    private var gameId: String? = ""
    private val listPseudoListeners = ArrayList<ValueEventListener>()

    override val layoutId = R.layout.fragment_game

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            gameId = arguments?.getString(KEY_GAME_ID)
            if (gameId != null) {
                loadPlayers()
            }
        }
    }

    private fun loadPlayers() {
        disposables.add(
            realm.where(
                Game.class).equalTo("idGame", this.gameId).findAll().asFlowable().subscribe({
                    new FragmentGame $loadPlayers$2(this)
                }, {
                    FragmentGame$loadPlayers$3.INSTANCE)
                })
            )
                    FragmentGame $loadPlayers$event$1 event = new FragmentGame$loadPlayers$event$1(this)
        mDbRef?.child(TABLE_GAMES)?.child(gameId ?: "")?.child(TABLE_PLAYERS)?.addValueEventListener(event)
    }


    private fun changeDisplayOfGame(playersList: ArrayList<GamePlayer>) {
        if (playersList.isNotEmpty()) {
            when (playersList.count()) {
                1 -> {
                    loadOnePlayer(playersList)
                }
                2 -> {
                    loadTwoPlayers(playersList)
                }
                3 -> {
                    loadThreePlayers(playersList)
                }
                4 -> {
                    loadFourPlayers(playersList)
                }
                else -> {
                    Timber.w("Impossible de charger la partie si le nombre de joueurs n'est pas une valeur entre 1 et 4")
                }
            }
        }
    }

    private fun loadOnePlayer(playerList: ArrayList<GamePlayer>) {
        linearLayoutTopPlayer.gone()
        linearLayoutLeftPlayer.gone()
        linearLayoutRightPlayer.gone()
        textViewTopPlayer.gone()
        textViewLeftPlayer.gone()
        textViewRightPlayer.gone()
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()

        setPlayerName(playerList[0].idPlayer ?: "", 1)
    }

    private fun loadTwoPlayers(playerList: ArrayList<GamePlayer>) {
        linearLayoutLeftPlayer.gone()
        linearLayoutRightPlayer.gone()
        textViewLeftPlayer.gone()
        textViewRightPlayer.gone()
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()
        linearLayoutTopPlayer.show()
        textViewTopPlayer.show()

        var mainPlayer: GamePlayer? = null
        for (player in playerList) {
            if (player.leader == true) {
                mainPlayer = player
                playerList.remove(player)
                break
            }
        }

        if (mainPlayer == null) {
            mainPlayer = playerList[0]
            playerList.removeAt(0)
        }

        setPlayerName(mainPlayer.idPlayer ?: "", POSITION_BOT)
        setPlayerName(playerList[0].idPlayer ?: "", POSITION_TOP)
    }

    private fun loadThreePlayers(playerList: ArrayList<GamePlayer>) {
        linearLayoutTopPlayer.gone()
        textViewTopPlayer.gone()
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()
        linearLayoutLeftPlayer.show()
        textViewLeftPlayer.show()
        linearLayoutRightPlayer.show()
        textViewRightPlayer.show()

        var mainPlayer: GamePlayer? = null
        for (player in playerList) {
            if (player.leader == true) {
                mainPlayer = player
                playerList.remove(player)
                break
            }
        }

        if (mainPlayer == null) {
            mainPlayer = playerList[0]
            playerList.removeAt(0)
        }

        setPlayerName(mainPlayer.idPlayer ?: "", POSITION_BOT)
        setPlayerName(playerList[0].idPlayer ?: "", POSITION_LEFT)
        setPlayerName(playerList[1].idPlayer ?: "", POSITION_RIGHT)
    }

    private fun loadFourPlayers(playerList: ArrayList<GamePlayer>) {
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()
        linearLayoutLeftPlayer.show()
        textViewLeftPlayer.show()
        linearLayoutRightPlayer.show()
        textViewRightPlayer.show()
        linearLayoutTopPlayer.show()
        textViewTopPlayer.show()

        var mainPlayer: GamePlayer? = null
        for (player in playerList) {
            if (player.leader == true) {
                mainPlayer = player
                playerList.remove(player)
                break
            }
        }

        if (mainPlayer == null) {
            mainPlayer = playerList[0]
            playerList.removeAt(0)
        }

        setPlayerName(mainPlayer.idPlayer ?: "", POSITION_BOT)
        setPlayerName(playerList[0].idPlayer ?: "", POSITION_LEFT)
        setPlayerName(playerList[1].idPlayer ?: "", POSITION_RIGHT)
        setPlayerName(playerList[2].idPlayer ?: "", POSITION_TOP)
    }

    private fun setPlayerName(idPlayer: String, position: Int) {
        FragmentGame$setPlayerName$listenerNbPlayersInGame$1 listenerNbPlayersInGame = new FragmentGame$setPlayerName$listenerNbPlayersInGame$1(this, position)

        listPseudoListeners.add(listenerNbPlayersInGame)

        mDbRef?.child("players")?.child(idPlayer)?.addValueEventListener(listenerNbPlayersInGame)
    }

    private fun setPlayerNameAtRightPosition(playerName: String, position: Int) {
        when (position) {
            0 -> {
                textViewTopPlayer.text = playerName
            }
            1 -> {
                textViewBotPlayer.text = playerName
            }
            2 -> {
                textViewLeftPlayer.text = playerName
            }
            3 -> {
                textViewRightPlayer.text = playerName
            }
            else -> {

            }
        }
    }
}