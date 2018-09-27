package fr.skyle.capito

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric

/**
 * Created by Openium on 20/03/2018.
 */
class ApplicationImpl : CustomApplication() {

    override fun initializeCrashlytics() {
        val core = CrashlyticsCore.Builder().disabled(true).build()
        val crashlytics = Crashlytics.Builder().core(core).build()
        Fabric.with(this, crashlytics)
    }
}