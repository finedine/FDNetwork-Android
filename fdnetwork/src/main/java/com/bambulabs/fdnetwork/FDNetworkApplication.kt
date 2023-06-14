package com.bambulabs.fdnetwork

import android.app.Application
import com.bambulabs.fdnetwork.FDNetworkCompanions.Companion.fdNetworkApplication

class FDNetworkApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        fdNetworkApplication = this
    }
}