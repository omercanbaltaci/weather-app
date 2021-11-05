package com.example.finalproject.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class ConnectionLiveData(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )

    private val networkCallBack = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            postValue(false)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), networkCallBack)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }
}