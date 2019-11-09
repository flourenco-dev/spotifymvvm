package com.fabiolourenco.spotifymvvm.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fabiolourenco.spotifymvvm.data.RepositoryFactory

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    // Use DataRepository instance to handle app data from Network or DB
    private val dataRepository = RepositoryFactory.getDataRepository()

    fun updateToken(token: String, expiresIn: Int) {
        dataRepository.setToken(token, expiresIn)
    }
}