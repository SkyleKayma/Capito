package fr.skyle.cardgame.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.cardgame.R
import fr.skyle.cardgame.fragment.FragmentSetPseudo

class ActivitySetPseudo : AbstractActivityFragment() {

    override val layoutId: Int
        get() = R.layout.container_no_toolbar

    override fun getDefaultFragment(): Fragment? {
        return FragmentSetPseudo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Choix de pseudo"
    }
}