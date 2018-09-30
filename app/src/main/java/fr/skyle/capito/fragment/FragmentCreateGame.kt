package fr.skyle.capito.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import fr.openium.kotlintools.ext.startActivity
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.capito.*
import fr.skyle.capito.activity.ActivityGame
import fr.skyle.capito.model.Game
import fr.skyle.capito.model.GamePlayer
import kotlinx.android.synthetic.main.fragment_create_game.*
import timber.log.Timber


class FragmentCreateGame : AbstractFragmentFirebase() {

    override val layoutId: Int
        get() = R.layout.fragment_create_game

    private var gameId: String? = ""
    private var gameName: String? = ""

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonCreateGame.setOnClickListener {
            if (checkIfGameNameIsCorrect()) {
                buttonCreateGame.startAnimation()
                createANewGame()
            }
        }
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun checkIfGameNameIsCorrect(): Boolean {
        if (editTextGameName.textTrimmed() == "") {
            Toast.makeText(context, getString(R.string.create_game_please_fill_game_name), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createANewGame() {
        createGame()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("[INFO] createGame: SUCCESS")

                addCurrentGameToPlayer()?.addOnCompleteListener { taskAddPlayer ->
                    if (taskAddPlayer.isSuccessful) {
                        Timber.d("[INFO] addCurrentGameToPlayer: SUCCESS")
                        Toast.makeText(context, getString(R.string.create_game_creation_successful), Toast.LENGTH_SHORT).show()

                        startActivity<ActivityGame>(Bundle().apply {
                            putString(KEY_GAME_ID, gameId)
                            putString(KEY_GAME_NAME, gameName)
                        })

                        activity?.finish()
                    } else {
                        Timber.d("[INFO] addCurrentGameToPlayer: FAILURE with ${taskAddPlayer.exception}")
                        Toast.makeText(context, getString(R.string.create_game_creation_failed), Toast.LENGTH_SHORT).show()

                        //Probably no network available
                        buttonCreateGame.revertAnimation()
                    }
                }
            } else {
                Timber.w("[INFO] createGame: FAILURE with ${task.exception}")
                Toast.makeText(context, getString(R.string.create_game_creation_failed), Toast.LENGTH_SHORT).show()

                //Probably no network available
                buttonCreateGame.revertAnimation()
            }
        }
    }

    private fun createGame(): Task<Void>? {
        //Game name start with a capitalize letter
        gameName = editTextGameName.textTrimmed().substring(0, 1).toUpperCase() + editTextGameName.textTrimmed().substring(1)

        //Directly add this player to the game
        val players = hashMapOf<String, GamePlayer>()
        players[mAuth?.currentUser?.uid ?: ""] = GamePlayer(leader = false)

        val push = mDbRef?.child(TABLE_GAMES)?.push()
        gameId = push?.key

        val game = Game(name = gameName, started = false, players = players)
        return mDbRef?.child(TABLE_GAMES)?.child(gameId ?: "")?.setValue(game)
    }

    private fun addCurrentGameToPlayer(): Task<Void>? {
        return mDbRef?.child(TABLE_PLAYERS)?.child(
            mAuth?.currentUser?.uid
                ?: ""
        )?.child("currentGame")?.setValue(gameId)
    }
}