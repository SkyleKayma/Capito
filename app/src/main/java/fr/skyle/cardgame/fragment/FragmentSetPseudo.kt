package fr.skyle.cardgame.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import fr.openium.kotlintools.ext.startActivity
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.cardgame.R
import fr.skyle.cardgame.activity.ActivityMain
import fr.skyle.cardgame.model.Player
import kotlinx.android.synthetic.main.fragment_set_pseudo.*
import timber.log.Timber


class FragmentSetPseudo : AbstractFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_set_pseudo

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonSetPseudo.setOnClickListener {
            buttonSetPseudo.startAnimation()

            val uid = mAuth?.currentUser?.uid
            if (uid != null) {
                val taskAccountCreation = createUserInFirebaseDB()

                taskAccountCreation.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.w("[INFO] addUserToTheDB: SUCCESS")
                        Toast.makeText(context, "Création de compte réussie.", Toast.LENGTH_SHORT).show()
                        goToMainPage()
                    } else {
                        Timber.w("[INFO] addUserToTheDB: FAILURE with ${task.exception}")
                        Toast.makeText(context, "Création de compte impossible.", Toast.LENGTH_SHORT).show()
                        buttonSetPseudo.revertAnimation()
                    }
                }
            }
        }
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun createUserInFirebaseDB(): Task<Void> {
        val player = Player(editTextSetPseudo.textTrimmed(), 0, 0)
        return mDatabase!!.child("players").child(mAuth?.currentUser?.uid!!).setValue(player.toHashMap())
    }

    private fun goToMainPage() {
        startActivity<ActivityMain>()
        activity?.finish()
    }
}