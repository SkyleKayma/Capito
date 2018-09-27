package fr.skyle.capito.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.capito.R
import fr.skyle.capito.fragment.FragmentCreateGame

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

        supportActionBar?.title = getString(R.string.create_game_screen_title)
    }
}