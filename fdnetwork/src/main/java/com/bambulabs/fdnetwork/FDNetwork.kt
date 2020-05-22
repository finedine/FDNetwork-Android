package com.bambulabs.fdnetwork

import android.app.Application
import android.os.Build
import com.androidnetworking.AndroidNetworking
import okhttp3.OkHttpClient
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

class FDNetwork private constructor(application: Application) {
    init {

    }

    companion object : SingletonHolder<FDNetwork, Application>(::FDNetwork)
}