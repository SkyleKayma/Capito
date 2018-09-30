package fr.skyle.capito.fragment

import android.os.Bundle
import android.os.Handler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.openium.kotlintools.ext.startActivity
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_PLAYERS
import fr.skyle.capito.activity.ActivityLogin
import fr.skyle.capito.activity.ActivityMain
import fr.skyle.capito.activity.ActivitySetPseudo
import timber.log.Timber


class FragmentSplash : AbstractFragmentFirebase() {

    override val layoutId: Int
        get() = R.layout.fragment_splash

    private var nbActiveTasks: Int = 2

    private var actualPlayerState: Int = PLAYER_STATE_NOT_EXISTING

    companion object {
        const val PLAYER_STATE_NOT_EXISTING = 0
        const val PLAYER_STATE_EXISTING_WITH_PSEUDO = 1
        const val PLAYER_STATE_EXISTING_WITHOUT_PSEUDO = 2
    }

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Get current user
        val currentUser = mAuth!!.currentUser

        //Check if the user exist
        if (currentUser != null) {
            //We need to check if user is in the DB too to know if wizard as finished last time
            checkIfUserExistInDB(currentUser.uid)
        } else {
            //If not, login is needed
            actualPlayerState = PLAYER_STATE_NOT_EXISTING
            nbActiveTasks -= 1
            goToNextPage()
        }

        //Need to wait atleast 3s on splash screen
        Handler().postDelayed({
            nbActiveTasks -= 1
            goToNextPage()
        }, 3000)
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun checkIfUserExistInDB(uid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                actualPlayerState = if (dataSnapshot.child(TABLE_PLAYERS).child(uid).exists()) {
                    PLAYER_STATE_EXISTING_WITH_PSEUDO
                } else {
                    PLAYER_STATE_EXISTING_WITHOUT_PSEUDO
                }
                nbActiveTasks -= 1
                goToNextPage()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.d("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        valueEventListeners.add(postListener)
        mDbRef?.addListenerForSingleValueEvent(postListener)
    }

    private fun goToNextPage() {
        if (nbActiveTasks == 0) {
            when (actualPlayerState) {
                PLAYER_STATE_NOT_EXISTING -> {
                    startActivity<ActivityLogin>()
                }
                PLAYER_STATE_EXISTING_WITHOUT_PSEUDO -> {
                    startActivity<ActivitySetPseudo>()
                }
                PLAYER_STATE_EXISTING_WITH_PSEUDO -> {
                    startActivity<ActivityMain>()
                }
            }
            activity?.finish()
        }
    }
}