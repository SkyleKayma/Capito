package fr.skyle.capito.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.capito.R
import fr.skyle.capito.fragment.FragmentSetPseudo

class ActivitySetPseudo : AbstractActivityFragment() {

    override fun getDefaultFragment(): Fragment? {
        return FragmentSetPseudo()
    }

    override val showHomeAsUp: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.set_pseudo_screen_title)
    }
}