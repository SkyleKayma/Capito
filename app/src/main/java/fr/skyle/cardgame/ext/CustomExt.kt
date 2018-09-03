package fr.skyle.cardgame.ext

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Openium on 20/03/2018.
 */

val Context.hasNetwork: Boolean
    get() {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }