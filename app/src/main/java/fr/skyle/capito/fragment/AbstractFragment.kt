package fr.skyle.capito.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Openium on 20/03/2018.
 */

abstract class AbstractFragment : Fragment() {

    protected var disposables: CompositeDisposable = CompositeDisposable()

    protected abstract val layoutId: Int

    // =================================================================================================================
    // Life cycle
    // =================================================================================================================

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = layoutId
        return inflater.inflate(layoutId, container, false)
    }
}
