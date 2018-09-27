package fr.skyle.capito.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import fr.skyle.capito.R
import fr.skyle.capito.fragment.OnBackPressedListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Openium on 20/03/2018.
 */

abstract class AbstractActivity : AppCompatActivity() {
    protected val disposables: CompositeDisposable = CompositeDisposable()
    protected open val handleFragmentBackPressed: Boolean = true

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        setContentView(layoutId)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }

        setHomeAsUp(showHomeAsUp)
    }

    protected open fun startSubscription(onStartSubscriptions: CompositeDisposable) {

    }

    protected fun setHomeAsUp(enabled: Boolean = true) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(enabled)
            setHomeButtonEnabled(enabled)
        }
    }

    open protected fun beforeSetContentView() {

    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onArrowPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    open fun onArrowPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (handleFragmentBackPressed) {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container_framelayout)
            if (currentFragment !is OnBackPressedListener || !currentFragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    // Si true la fleche back est affichée
    protected open val showHomeAsUp: Boolean = false

    // Retourne le layout qui est associé à l'activité
    protected open val layoutId: Int = R.layout.container_toolbar
}
