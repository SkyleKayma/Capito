package fr.skyle.capito.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.capito.R
import fr.skyle.capito.fragment.FragmentMain

class ActivityMain : AbstractActivityFragment() {

    override val layoutId: Int
        get() = R.layout.container_toolbar

    override fun getDefaultFragment(): Fragment? {
        return FragmentMain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.home_screen_title)
    }
}