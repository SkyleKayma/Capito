package fr.skyle.capito

import android.app.Application
import com.crashlytics.android.Crashlytics
import fr.skyle.capito.log.CrashReportingTree
import io.fabric.sdk.android.Fabric
import timber.log.Timber


/**
 * Created by Openium on 20/03/2018.
 */

abstract class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeCrashlytics()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    open fun initializeCrashlytics() {
        Fabric.with(applicationContext, Crashlytics())
    }
}
