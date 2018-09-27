package fr.skyle.capito.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import fr.openium.kotlintools.ext.startActivity
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_PLAYERS
import fr.skyle.capito.activity.ActivityMain
import fr.skyle.capito.model.User
import kotlinx.android.synthetic.main.fragment_set_pseudo.*
import timber.log.Timber


class FragmentSetPseudo : AbstractFragmentFirebase() {

    override val layoutId: Int
        get() = R.layout.fragment_set_pseudo

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonSetPseudo.setOnClickListener {
            buttonSetPseudo.startAnimation()

            //Task to handle user creation
            createUserInFirebaseDB().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.w("[INFO] addUserToTheDB: SUCCESS")
                    Toast.makeText(context, getString(R.string.set_pseudo_account_creation_successful), Toast.LENGTH_SHORT).show()

                    //Go into the app
                    goToMainPage()
                } else {
                    Timber.w("[INFO] addUserToTheDB: FAILURE with ${task.exception}")
                    Toast.makeText(context, getString(R.string.set_pseudo_account_creation_failed), Toast.LENGTH_SHORT).show()

                    //Something went wrong, maybe no network available
                    buttonSetPseudo.revertAnimation()
                }
            }
        }
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun createUserInFirebaseDB(): Task<Void> {
        val player = User(editTextSetPseudo.textTrimmed(), 0, 0)
        return mDbRef!!.child(TABLE_PLAYERS).child(mAuth?.currentUser?.uid!!).setValue(player)
    }

    private fun goToMainPage() {
        startActivity<ActivityMain>()
        activity?.finish()
    }
}