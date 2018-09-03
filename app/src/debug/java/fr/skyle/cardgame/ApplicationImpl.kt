package fr.skyle.cardgame

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import fr.skyle.cardgame.injection.DebugModules
import io.fabric.sdk.android.Fabric
import io.realm.RealmConfiguration

/**
 * Created by Openium on 20/03/2018.
 */
class ApplicationImpl : CustomApplication() {
    override val kodein: Kodein by Kodein.lazy {
        extend(super.kodein)
        import(DebugModules.configModule, true)
        import(DebugModules.restModule, true)
        import(DebugModules.serviceModule, true)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun initRealm(builder: RealmConfiguration.Builder): RealmConfiguration.Builder {
        val newBuilder = super.initRealm(builder)
        return if (instance<Boolean>("mock")) newBuilder.inMemory() else newBuilder
    }

    override fun initializeCrashlytics() {
        val core = CrashlyticsCore.Builder().disabled(true).build()
        val crashlytics = Crashlytics.Builder().core(core).build()
        Fabric.with(this, crashlytics)
    }
}