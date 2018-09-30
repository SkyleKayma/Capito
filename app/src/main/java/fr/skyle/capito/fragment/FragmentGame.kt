package fr.skyle.capito.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.openium.kotlintools.ext.gone
import fr.openium.kotlintools.ext.show
import fr.skyle.capito.KEY_GAME_ID
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_GAMES
import fr.skyle.capito.TABLE_PLAYERS
import fr.skyle.capito.model.Game
import fr.skyle.capito.model.GamePlayer
import fr.skyle.capito.model.User
import kotlinx.android.synthetic.main.fragment_game.*
import timber.log.Timber


class FragmentGame : AbstractFragmentFirebase() {

    companion object {
        const val POSITION_TOP = 0
        const val POSITION_BOT = 1
        const val POSITION_LEFT = 2
        const val POSITION_RIGHT = 3
    }

    private var gameId: String? = ""
    private val listPseudoListeners = mutableListOf<ValueEventListener>()

    override val layoutId = R.layout.fragment_game

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            gameId = arguments?.getString(KEY_GAME_ID)
            if (gameId != null) {
                initAllListeners()
            } else {
                activity?.finish()
            }
        } else {
            activity?.finish()
        }
    }

    private fun initAllListeners() {
        //Listener for the game name
        val listenerGameName = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameName = dataSnapshot.getValue(String::class.java)

                //Update game name
                (activity as AppCompatActivity?)?.supportActionBar?.title = getString(R.string.game_screen_title, gameName ?: "")
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                Toast.makeText(context!!, getString(R.string.home_cant_get_game_list), Toast.LENGTH_SHORT).show()
            }
        }
        valueEventListeners.add(listenerGameName)
        mDbRef?.child(TABLE_GAMES)?.child(gameId!!)?.child(Game::name.name)?.addValueEventListener(listenerGameName)

        //Listener for the game players
        val listenerGamePlayers = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val players = mutableListOf<GamePlayer>()
                for (item in dataSnapshot.children) {
                    val player = item.getValue(GamePlayer::class.java)
                    player?.idPlayer = item.key

                    if (player != null) {
                        players.add(player)
                    }
                }

                //Update players list actually displayed
                for (listener in listPseudoListeners) {
                    mDbRef?.removeEventListener(listener)
                }

                changeDisplayOfGamePlayers(players)
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                Toast.makeText(context!!, getString(R.string.home_cant_get_game_list), Toast.LENGTH_SHORT).show()
            }
        }
        valueEventListeners.add(listenerGamePlayers)
        mDbRef?.child(TABLE_GAMES)?.child(gameId!!)?.child(Game::players.name)?.addValueEventListener(listenerGamePlayers)
    }

    private fun changeDisplayOfGamePlayers(playersList: MutableList<GamePlayer>) {
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

    private fun loadOnePlayer(playerList: MutableList<GamePlayer>) {
        linearLayoutTopPlayer.gone()
        linearLayoutLeftPlayer.gone()
        linearLayoutRightPlayer.gone()
        textViewTopPlayer.gone()
        textViewLeftPlayer.gone()
        textViewRightPlayer.gone()
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()

        //There is only one player so it's by default the main player
        //The main player is always at the bottom of the screen
        setPlayerName(playerList[0].idPlayer ?: "", POSITION_BOT)
    }

    private fun loadTwoPlayers(playerList: MutableList<GamePlayer>) {
        linearLayoutLeftPlayer.gone()
        linearLayoutRightPlayer.gone()
        textViewLeftPlayer.gone()
        textViewRightPlayer.gone()
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()
        linearLayoutTopPlayer.show()
        textViewTopPlayer.show()

        //The main player is always at the bottom of the screen
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

    private fun loadThreePlayers(playerList: MutableList<GamePlayer>) {
        linearLayoutTopPlayer.gone()
        textViewTopPlayer.gone()
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()
        linearLayoutLeftPlayer.show()
        textViewLeftPlayer.show()
        linearLayoutRightPlayer.show()
        textViewRightPlayer.show()

        //The main player is always at the bottom of the screen
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

    private fun loadFourPlayers(playerList: MutableList<GamePlayer>) {
        linearLayoutBotPlayer.show()
        textViewBotPlayer.show()
        linearLayoutLeftPlayer.show()
        textViewLeftPlayer.show()
        linearLayoutRightPlayer.show()
        textViewRightPlayer.show()
        linearLayoutTopPlayer.show()
        textViewTopPlayer.show()

        //The main player is always at the bottom of the screen
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
        val listenerPlayer = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val playerName = dataSnapshot.getValue(String::class.java)
                setPlayerNameAtRightPosition(playerName ?: "", position)
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                Toast.makeText(context!!, getString(R.string.home_cant_refresh_players_name), Toast.LENGTH_SHORT).show()
            }
        }
        listPseudoListeners.add(listenerPlayer)
        mDbRef?.child(TABLE_PLAYERS)?.child(idPlayer)?.child(User::pseudo.name)?.addValueEventListener(listenerPlayer)
    }

    private fun setPlayerNameAtRightPosition(playerName: String, position: Int) {
        when (position) {
            POSITION_TOP -> {
                textViewTopPlayer.text = playerName
            }
            POSITION_BOT -> {
                textViewBotPlayer.text = playerName
            }
            POSITION_LEFT -> {
                textViewLeftPlayer.text = playerName
            }
            POSITION_RIGHT -> {
                textViewRightPlayer.text = playerName
            }
            else -> {
                Timber.d("Unknown position $position")
            }
        }
    }
}