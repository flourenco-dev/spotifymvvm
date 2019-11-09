package com.fabiolourenco.spotifymvvm

import android.app.Application
import com.fabiolourenco.spotifymvvm.utils.AppConstants
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import timber.log.Timber

class App : Application() {

    var spotifyAppRemote: SpotifyAppRemote? = null
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Set the connection parameters
        val connectionParams = ConnectionParams.Builder(AppConstants.SPOTIFY_CLIENT_ID)
            .setRedirectUri(AppConstants.getSpotifyRedirectUri(this).toString())
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(applicationContext, connectionParams, object : Connector.ConnectionListener {
            override fun onFailure(throwable: Throwable?) {
                Timber.w(throwable)
            }

            override fun onConnected(remote: SpotifyAppRemote?) {
                Timber.d("SpotifyAppRemote connected")
                remote?.let {
                    spotifyAppRemote = it
                }
            }

        })
    }

}