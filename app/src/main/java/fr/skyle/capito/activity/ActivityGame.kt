package fr.skyle.capito.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.capito.KEY_GAME_NAME
import fr.skyle.capito.R
import fr.skyle.capito.fragment.FragmentGame

class ActivityGame : AbstractActivityFragment() {

    override val showHomeAsUp: Boolean
        get() = true

    override fun getDefaultFragment(): Fragment? {
        return FragmentGame()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //We temporally set the game name, it will be refreshed when fragment will be loaded with game data
        val gameName = intent?.getStringExtra(KEY_GAME_NAME)
        supportActionBar?.title = getString(R.string.game_screen_title, gameName)
    }
}