package fr.skyle.cardgame.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.openium.kotlintools.ext.gone
import fr.openium.kotlintools.ext.show
import fr.openium.kotlintools.ext.startActivity
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.cardgame.R
import fr.skyle.cardgame.activity.ActivityMain
import fr.skyle.cardgame.activity.ActivitySetPseudo
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber


class FragmentLogin : AbstractFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_login

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            if (mAuth?.currentUser?.uid != null) {
                checkIfPlayerExistInDB(mAuth?.currentUser?.uid!!)
            }
        } else {
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
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("[INFO] signInWithEmail: SUCCESS")

                        //Check if user exist in DB
                        if (mAuth?.currentUser?.uid != null) {
                            checkIfPlayerExistInDB(mAuth?.currentUser?.uid!!)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.d("[INFO] signInWithEmail: FAILURE with ${task.exception}")
                        Toast.makeText(context, "Vous n'avez pas de compte, création en cours...", Toast.LENGTH_SHORT).show()

                        //Try to create an account
                        createAccount()
                    }
                }
    }

    private fun createAccount() {
        mAuth!!.createUserWithEmailAndPassword(editTextLoginEmail.textTrimmed(), editTextLoginPassword.textTrimmed())
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("[INFO] createUserWithEmail: SUCCESS")

                        goToSetPseudoPage()
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w("[INFO] createUserWithEmail: FAILURE with ${task.exception}")
                        Toast.makeText(context, "Création de compte impossible.", Toast.LENGTH_SHORT).show()
                        buttonLoginConnect.revertAnimation()
                    }
                }
    }

    private fun checkIfPlayerExistInDB(uid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val isPlayerAllreadyExist = dataSnapshot.child("players").child(uid).exists()
                if (isPlayerAllreadyExist) {
                    goToMainPage()
                } else {
                    goToSetPseudoPage()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Timber.w("loadPost:onCancelled ${databaseError.toException()}")
                // ...
            }
        }
        mDatabase?.addListenerForSingleValueEvent(postListener)

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