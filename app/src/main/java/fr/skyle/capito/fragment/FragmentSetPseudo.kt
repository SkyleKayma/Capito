package fr.skyle.capito.fragment

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import fr.openium.kotlintools.ext.startActivity
import fr.openium.kotlintools.ext.textTrimmed
import fr.skyle.capito.KEY_USER_EMAIL
import fr.skyle.capito.KEY_USER_PASSWORD
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_PLAYERS
import fr.skyle.capito.activity.ActivityMain
import fr.skyle.capito.ext.hasNetwork
import fr.skyle.capito.model.User
import kotlinx.android.synthetic.main.fragment_set_pseudo.*
import timber.log.Timber


class FragmentSetPseudo : AbstractFragmentFirebase() {

    override val layoutId: Int
        get() = R.layout.fragment_set_pseudo

    private var email: String? = null
    private var password: String? = null

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null) {
            email = arguments!!.getString(KEY_USER_EMAIL) ?: ""
            password = arguments!!.getString(KEY_USER_PASSWORD) ?: ""

            if (email != "" && password != "") {
                buttonSetPseudo.setOnClickListener {
                    if (checkIfPseudoIsCorrect()) {
                        buttonSetPseudo.startAnimation()
                        createAccount()
                    }
                }
            } else {
                activity?.finish()
            }
        } else {
            activity?.finish()
        }
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOBS ---
    // ---------------------------------------------------

    private fun checkIfPseudoIsCorrect(): Boolean {
        if (editTextSetPseudo.textTrimmed() == "") {
            Toast.makeText(context, getString(R.string.set_pseudo_please_fill_pseudo), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createAccount() {
        if (context?.hasNetwork == true) {
            mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        Timber.d("createUserWithEmail: SUCCESS")

                        addUserToFirebase()
                    } else {
                        Timber.d("createUserWithEmail: FAILURE with ${task.exception}")

                        //Failure, so this account probably already exist
                        activity?.finish()
                    }
                }
        } else {
            buttonSetPseudo.revertAnimation()
            Toast.makeText(context, getString(R.string.generic_no_network), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUserToFirebase() {
        //Task to handle user creation
        createUserInFirebaseDB()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.w("[INFO] addUserToTheDB: SUCCESS")
                Toast.makeText(context, getString(R.string.set_pseudo_account_creation_successful), Toast.LENGTH_SHORT).show()

                //Go into the app
                goToMainPage()
            } else {
                Timber.w("[INFO] addUserToTheDB: FAILURE with ${task.exception}")
                Toast.makeText(context, getString(R.string.set_pseudo_account_creation_failed), Toast.LENGTH_SHORT).show()

                activity?.finish()
            }
        }
    }

    private fun createUserInFirebaseDB(): Task<Void>? {
        val player = User(editTextSetPseudo.textTrimmed(), 0, 0)
        return mDbRef?.child(TABLE_PLAYERS)?.child(mAuth?.currentUser?.uid ?: "")?.setValue(player)
    }

    // ---------------------------------------------------
    // --- OTHER JOBS ---
    // ---------------------------------------------------

    private fun goToMainPage() {
        startActivity<ActivityMain>()
        activity?.finish()
    }
}