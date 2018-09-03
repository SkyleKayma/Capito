package fr.skyle.cardgame.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.appKodein
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm


/**
 * Created by Openium on 20/03/2018.
 */

abstract class AbstractFragment : Fragment() {
    protected var oneTimeDisposables: CompositeDisposable = CompositeDisposable() //only subscribe one time and unsubscribe later
    protected var rebindDisposables: CompositeDisposable = CompositeDisposable() //Resubscribe in onstart

    protected var isAlive: Boolean = false

    open protected val customToolbarFragment: Toolbar? = null

    protected val injector = KodeinInjector()
    protected var realm: Realm? = null

    protected var mAuth: FirebaseAuth? = null
    protected var mDatabase: DatabaseReference? = null


    // =================================================================================================================
    // Life cycle
    // =================================================================================================================

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isAlive = true
        injector.inject(appKodein())
        realm = Realm.getDefaultInstance()
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutInflater = inflater

        val layoutId = layoutId
        val view: View = layoutInflater.inflate(layoutId, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        startSubscription(rebindDisposables)
    }

    override fun onStop() {
        super.onStop()
        rebindDisposables.clear()
    }

    protected open fun startSubscription(onStartSubscriptions: CompositeDisposable) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        oneTimeDisposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        oneTimeDisposables.clear()
    }

    override fun onDetach() {
        super.onDetach()
        isAlive = false
        realm?.close()
        realm = null
    }

    protected abstract val layoutId: Int

    inline fun ViewPropertyAnimatorCompat.withEndActionSafe(crossinline body: () -> Unit): ViewPropertyAnimatorCompat {
        return withEndAction {
            if (view != null) {
                body()
            }
        }
    }

    inline fun ViewPropertyAnimatorCompat.withStartActionSafe(crossinline body: () -> Unit): ViewPropertyAnimatorCompat {
        return withStartAction {
            if (view != null) {
                body()
            }
        }
    }
}
