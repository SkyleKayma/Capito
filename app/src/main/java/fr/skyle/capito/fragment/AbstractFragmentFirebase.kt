package fr.skyle.capito.fragment

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * Created by Openium on 20/03/2018.
 */

abstract class AbstractFragmentFirebase : AbstractFragment() {

    protected var mAuth: FirebaseAuth? = null
    protected var mDbRef: DatabaseReference? = null
    protected var valueEventListeners = mutableListOf<ValueEventListener>()
    protected var childEventListeners = mutableListOf<ChildEventListener>()

    // =================================================================================================================
    // Life cycle
    // =================================================================================================================

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
    }

    override fun onDestroy() {
        super.onDestroy()
        //Remove ValueEventListeners
        for (listener in valueEventListeners) {
            mDbRef?.removeEventListener(listener)
        }

        //Remove ChildEventListeners
        for (listener in childEventListeners) {
            mDbRef?.removeEventListener(listener)
        }
    }
}
