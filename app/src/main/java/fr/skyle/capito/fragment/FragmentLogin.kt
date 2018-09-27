package fr.skyle.capito.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.openium.kotlintools.ext.gone
import fr.openium.kotlintools.ext.show
import fr.openium.kotlintools.ext.startActivity
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_PLAYERS
import fr.skyle.capito.activity.ActivityMain
import fr.skyle.capito.activity.ActivitySetPseudo
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber


class FragmentLogin : AbstractFragmentFirebase() {

    override val layoutId: Int
        get() = R.layout.fragment_login

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
            checkIfUserExistInDB(mAuth?.currentUser?.uid!!)
        } else {
            //If not, login is needed
            linearLayoutProgressLogin.gone()
            scrollViewLogin.show()
        }

        buttonLoginConnect.setOnClickListener {
            buttonLoginConnect.startAnimation()
            connectUser()
        }
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun connectUser() {
        mAuth!!.signInWithEmailAndPassword(editTextLoginEmail.textTrimmed(), editTextLoginPassword.textTrimmed())
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        Timber.d("signInWithEmail: SUCCESS")

                        //Check if user exist in DB to know if wizard as finished last time
                        if (mAuth?.currentUser?.uid != null) {
                            checkIfUserExistInDB(mAuth?.currentUser?.uid!!)
                        }
                    } else {
                        Timber.d("signInWithEmail: FAILURE with ${task.exception}")
                        Toast.makeText(context, getString(R.string.login_no_account_creation_pending), Toast.LENGTH_SHORT).show()

                        //Try to create an account with these information
                        createAccount()
                    }
                }
    }

    private fun createAccount() {
        mAuth!!.createUserWithEmailAndPassword(editTextLoginEmail.textTrimmed(), editTextLoginPassword.textTrimmed())
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        Timber.d("createUserWithEmail: SUCCESS")
                        goToSetPseudoPage()
                    } else {
                        Timber.d("createUserWithEmail: FAILURE with ${task.exception}")
                        Toast.makeText(context, getString(R.string.login_account_creation_failed), Toast.LENGTH_SHORT).show()

                        //Failure, probably no network connection, just revert the button state
                        buttonLoginConnect.revertAnimation()
                    }
                }
    }

    private fun checkIfUserExistInDB(uid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isPlayerAlreadyExisting = dataSnapshot.child(TABLE_PLAYERS).child(uid).exists()
                if (isPlayerAlreadyExisting) {
                    //The user already exist in DB, go next step
                    goToMainPage()
                } else {
                    //The user is not in the DB so he needs a pseudo to create his account
                    goToSetPseudoPage()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.d("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        valueEventListeners.add(postListener)
        mDbRef?.addListenerForSingleValueEvent(postListener)
    }

    private fun goToSetPseudoPage() {
        startActivity<ActivitySetPseudo>()
        activity?.finish()
    }

    private fun goToMainPage() {
        startActivity<ActivityMain>()
        activity?.finish()
    }
}