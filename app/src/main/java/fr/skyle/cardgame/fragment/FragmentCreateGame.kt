package fr.skyle.cardgame.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.cardgame.R
import fr.skyle.cardgame.model.Game
import fr.skyle.cardgame.model.GamePlayer
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_create_game.*
import timber.log.Timber


class FragmentCreateGame : AbstractFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_create_game

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonCreateGame.setOnClickListener {
            buttonCreateGame.startAnimation()
            val taskAccountCreation = createGame()

            taskAccountCreation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.w("[INFO] createGame: SUCCESS")
                    Toast.makeText(context, "La partie a été crée.", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                } else {
                    Timber.w("[INFO] createGame: FAILURE with ${task.exception}")
                    Toast.makeText(context, "Impossible de créer la partie", Toast.LENGTH_SHORT).show()
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

        val players = RealmList<GamePlayer>()
        players.add(GamePlayer(mAuth?.currentUser?.uid ?: "", false, true))

        val push = mDatabase!!.child("games").push()

        val game = Game(push.key!!, gameName, false, players)
        return mDatabase!!.child("games").child(game.idGame).setValue(game.toHashMap())
    }
}