package fr.skyle.cardgame.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.widget.RxTextView
import fr.openium.kotlintools.ext.gone
import fr.openium.kotlintools.ext.show
import fr.openium.kotlintools.ext.startActivity
import fr.skyle.cardgame.R
import fr.skyle.cardgame.activity.ActivityCreateGame
import fr.skyle.cardgame.adapter.AdapterGame
import fr.skyle.cardgame.model.Game
import fr.skyle.cardgame.rest.model.RestGame
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FragmentMain : AbstractFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_main

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set layoutManager
        recyclerViewGameList.layoutManager = LinearLayoutManager(context)

        //Set listeners on Game Object in DB
        oneTimeDisposables.add(realm!!.where(Game::class.java).findAll().asFlowable().subscribe { games ->
            if (games.isEmpty()) {
                showEmptyView()
            } else {
                showData()

                if (recyclerViewGameList?.adapter == null) {
                    recyclerViewGameList.adapter = AdapterGame(context!!, games) { gameId ->
                        //TODO
                    }
                } else {
                    val list = getFilteredList(editTextSearchGame.editableText.toString())
                    (recyclerViewGameList?.adapter as AdapterGame).games = list
                    recyclerViewGameList?.adapter?.notifyDataSetChanged()
                }

                setSearchListeners()
            }
        })

        iniListeners()
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOB ---
    // ---------------------------------------------------

    private fun iniListeners() {
        buttonMainCreateGame.setOnClickListener {
            goToCreateGameActivity()
        }

        val listenerGame = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                realm?.executeTransaction {
                    it.where(Game::class.java)?.findAll()?.deleteAllFromRealm()
                }

                val gameList = arrayListOf<Game>()
                for (item in dataSnapshot.children) {
                    val restGame = item.getValue(RestGame::class.java)

                    if (restGame != null && item.key != null) {
                        val game = restGame.toGame(item.key!!)
                        gameList.add(game)
                    }
                }
                realm?.executeTransaction {
                    it.insertOrUpdate(gameList)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                Toast.makeText(context!!, "Récupération des parties impossible", Toast.LENGTH_SHORT).show()
            }
        }

        mDatabase?.child("games")?.limitToLast(20)?.addValueEventListener(listenerGame)
    }

    private fun setSearchListeners() {
        oneTimeDisposables.add(RxTextView.afterTextChangeEvents(editTextSearchGame)
                .debounce(20, TimeUnit.MILLISECONDS)
                .subscribe({
                    activity?.runOnUiThread {
                        val list = getFilteredList(it.editable().toString())
                        (recyclerViewGameList?.adapter as AdapterGame).games = list
                        recyclerViewGameList?.adapter?.notifyDataSetChanged()
                    }
                }, { error ->
                    Timber.e(error)
                }))
    }

    private fun getFilteredList(filter: String): RealmResults<Game> {
        val list = realm!!.where(Game::class.java).contains(Game::name.name, filter).findAll()

        if (filter.isEmpty()) {
            return realm!!.where(Game::class.java).findAll()
        } else {
            return list
        }
    }

    // ---------------------------------------------------
    // --- OTHER JOBS ---
    // ---------------------------------------------------

    private fun goToCreateGameActivity() {
        startActivity<ActivityCreateGame>()
    }

    private fun showData() {
        linearLayoutEmptyView.gone()
        progressBarGameList.gone()
        recyclerViewGameList.show()
    }

    private fun showEmptyView() {
        linearLayoutEmptyView.show()
        progressBarGameList.gone()
        recyclerViewGameList.gone()
    }
}