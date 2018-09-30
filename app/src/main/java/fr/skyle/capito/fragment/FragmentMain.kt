package fr.skyle.capito.fragment

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
import fr.skyle.capito.R
import fr.skyle.capito.TABLE_GAMES
import fr.skyle.capito.activity.ActivityCreateGame
import fr.skyle.capito.adapter.AdapterGame
import fr.skyle.capito.model.Game
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FragmentMain : AbstractFragmentFirebase() {

    override val layoutId: Int
        get() = R.layout.fragment_main

    private val gameList: MutableList<Game> = mutableListOf()

    // ---------------------------------------------------
    // --- LIFE CYCLE ---
    // ---------------------------------------------------

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set layoutManager
        recyclerViewGameList.layoutManager = LinearLayoutManager(context)

        initListeners()
    }

    // ---------------------------------------------------
    // --- SPECIFIC JOB ---
    // ---------------------------------------------------

    private fun initListeners() {
        buttonMainCreateGame.setOnClickListener {
            goToCreateGameActivity()
        }

        //Add a listener to handle changing in game list on FirebaseDB
        val listenerGame = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gameList.clear()
                for (item in dataSnapshot.children) {
                    val game = item.getValue(Game::class.java)
                    if (game != null && item.key != null) {
                        gameList.add(game)
                    }
                }

                updateGameList()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                Toast.makeText(context!!, getString(R.string.home_cant_get_game_list), Toast.LENGTH_SHORT).show()
            }
        }
        valueEventListeners.add(listenerGame)
        mDbRef?.child(TABLE_GAMES)?.addValueEventListener(listenerGame)
    }

    private fun updateGameList() {
        if (gameList.isEmpty()) {
            showEmptyView()
        } else {
            showData()

            if (recyclerViewGameList?.adapter == null) {
                recyclerViewGameList.adapter = AdapterGame(context!!, gameList.takeLast(20).toMutableList()) { gameId ->
                    //TODO do something
                }
            } else {
                val list = getFilteredList(editTextSearchGame.editableText.toString())
                (recyclerViewGameList?.adapter as AdapterGame).gameList = list.takeLast(20).toMutableList()
                recyclerViewGameList?.adapter?.notifyDataSetChanged()
            }

            setSearchListeners()
        }
    }

    private fun setSearchListeners() {
        //Listen for changing in SearchView
        disposables.add(
            RxTextView.afterTextChangeEvents(editTextSearchGame)
                .debounce(20, TimeUnit.MILLISECONDS)
                .subscribe({
                    activity?.runOnUiThread {
                        val list = getFilteredList(it.editable().toString())
                        (recyclerViewGameList?.adapter as AdapterGame).gameList = list
                        recyclerViewGameList?.adapter?.notifyDataSetChanged()
                    }
                }, { error ->
                    Timber.e(error)
                })
        )
    }

    private fun getFilteredList(filter: String): MutableList<Game> {
        //Return each Game where the name contains the searched field
        val list = gameList.filter {
            it.name != null && it.name!!.toLowerCase().trim().contains(filter.toLowerCase().trim())
        }

        return if (filter.isEmpty()) {
            gameList
        } else {
            mutableListOf<Game>().apply {
                addAll(list)
            }
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