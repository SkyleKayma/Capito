package fr.skyle.capito.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.openium.kotlintools.ext.startActivity
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.capito.*
import fr.skyle.capito.activity.ActivityMain
import fr.skyle.capito.activity.ActivitySetPseudo
import fr.skyle.capito.ext.hasNetwork
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

        buttonLoginConnect.setOnClickListener {
            if (checkIfAllFieldsAreFilledCorrectly()) {
                buttonLoginConnect.startAnimation()
                connectUser()
            }
        }
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun checkIfAllFieldsAreFilledCorrectly(): Boolean {
        when {
            editTextLoginEmail.textTrimmed() == "" -> {
                Toast.makeText(context, getString(R.string.login_please_fill_email), Toast.LENGTH_SHORT).show()
                return false
            }
            !editTextLoginEmail.textTrimmed().matches(Regex(EMAIL_REGEX)) -> {
                Toast.makeText(context, getString(R.string.login_email_format_invalid), Toast.LENGTH_SHORT).show()
                return false
            }
            editTextLoginPassword.textTrimmed() == "" -> {
                Toast.makeText(context, getString(R.string.login_please_fill_password), Toast.LENGTH_SHORT).show()
                return false
            }
            editTextLoginPassword.textTrimmed().count() < 6 -> {
                Toast.makeText(context, getString(R.string.login_password_format_invalid, 6), Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

    private fun connectUser() {
        if (context?.hasNetwork == true) {
            mAuth?.signInWithEmailAndPassword(editTextLoginEmail.textTrimmed(), editTextLoginPassword.textTrimmed())
                ?.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        Timber.d("signInWithEmail: SUCCESS")

                        //Check if user exist in DB to know if wizard as finished last time
                        if (mAuth?.currentUser?.uid != null) {
                            checkIfUserExistInDB(mAuth?.currentUser?.uid!!)
                        } else {
                            buttonLoginConnect.revertAnimation()
                        }
                    } else {
                        Timber.d("signInWithEmail: FAILURE with ${task.exception}")

                        //We need to create an account with these information
                        goToSetPseudoPage()
                    }
                }
        } else {
            buttonLoginConnect.revertAnimation()
            Toast.makeText(context, getString(R.string.generic_no_network), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfUserExistInDB(uid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(TABLE_PLAYERS).child(uid).exists()) {
                    //The user already exist in DB, go main page
                    goToMainPage()
                } else {
                    //The user is not in the DB so he needs a pseudo to create his account
                    goToSetPseudoPage()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.d("loadPost:onCancelled ${databaseError.toException()}")
                buttonLoginConnect.revertAnimation()
            }
        }
        valueEventListeners.add(postListener)
        mDbRef?.addListenerForSingleValueEvent(postListener)
    }

    // ---------------------------------------------------
    // --- OTHER JOBS ---
    // ---------------------------------------------------

    private fun goToSetPseudoPage() {
        buttonLoginConnect.revertAnimation()
        startActivity<ActivitySetPseudo>(Bundle().apply {
            putString(KEY_USER_EMAIL, editTextLoginEmail.textTrimmed())
            putString(KEY_USER_PASSWORD, editTextLoginPassword.textTrimmed())
        })
    }

    private fun goToMainPage() {
        startActivity<ActivityMain>()
        activity?.finish()
    }
}