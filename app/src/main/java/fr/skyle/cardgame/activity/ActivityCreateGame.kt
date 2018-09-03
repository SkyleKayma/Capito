package fr.skyle.cardgame.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.cardgame.R
import fr.skyle.cardgame.fragment.FragmentCreateGame

class ActivityCreateGame : AbstractActivityFragment() {

    override val layoutId: Int
        get() = R.layout.container_toolbar

    override val showHomeAsUp: Boolean
        get() = true

    override fun getDefaultFragment(): Fragment? {
        return FragmentCreateGame()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Créer une partie"
    }
}