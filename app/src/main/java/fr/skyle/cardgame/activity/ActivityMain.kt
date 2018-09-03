package fr.skyle.cardgame.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.cardgame.R
import fr.skyle.cardgame.fragment.FragmentMain

class ActivityMain : AbstractActivityFragment() {

    override val layoutId: Int
        get() = R.layout.container_toolbar

    override fun getDefaultFragment(): Fragment? {
        return FragmentMain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Liste des parties"
    }
}