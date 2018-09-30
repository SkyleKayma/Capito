package fr.skyle.capito.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import fr.skyle.capito.R
import fr.skyle.capito.fragment.FragmentSplash

class ActivitySplash : AbstractActivityFragment() {

    override val layoutId: Int
        get() = R.layout.container_no_toolbar

    override fun getDefaultFragment(): Fragment? {
        return FragmentSplash()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Splash"
    }
}