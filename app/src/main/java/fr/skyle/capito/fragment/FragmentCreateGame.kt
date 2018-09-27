package fr.skyle.capito.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_GAMES
import fr.skyle.capito.model.Game
import fr.skyle.capito.model.GamePlayer
import kotlinx.android.synthetic.main.fragment_create_game.*
import timber.log.Timber


class FragmentCreateGame : AbstractFragmentFirebase() {

    override val layoutId: Int
        get() = R.layout.fragment_create_game

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonCreateGame.setOnClickListener {
            buttonCreateGame.startAnimation()
            createGame().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.w("[INFO] createGame: SUCCESS")
                    Toast.makeText(context, getString(R.string.create_game_creation_successful), Toast.LENGTH_SHORT).show()

                    //Congratz, game created
                    activity?.onBackPressed()
                } else {
                    Timber.w("[INFO] createGame: FAILURE with ${task.exception}")
                    Toast.makeText(context, getString(R.string.create_game_creation_failed), Toast.LENGTH_SHORT).show()

                    //Probably no network available
                    buttonCreateGame.revertAnimation()
                }
            }
        }
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun createGame(): Task<Void> {
        val gameName = editTextGameName.textTrimmed()

        val players = hashMapOf<String, GamePlayer>()
        players[mAuth?.currentUser?.uid ?: ""] = GamePlayer(leader = false)

        val push = mDbRef!!.child(TABLE_GAMES).push()

        val game = Game(name = gameName, started = false, players = players)
        return mDbRef!!.child(TABLE_GAMES).child(push.key ?: "").setValue(game)
    }
}