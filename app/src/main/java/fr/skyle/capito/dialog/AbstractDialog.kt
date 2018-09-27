package fr.skyle.capito.dialog

import android.support.v7.app.AppCompatDialogFragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Openium on 20/03/2018.
 */
abstract class AbstractDialog : AppCompatDialogFragment() {
    protected val disposables: CompositeDisposable = CompositeDisposable()

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}