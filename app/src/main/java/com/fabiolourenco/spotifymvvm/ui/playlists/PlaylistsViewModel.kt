package com.fabiolourenco.spotifymvvm.ui.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.fabiolourenco.spotifymvvm.data.RepositoryFactory
import com.fabiolourenco.spotifymvvm.data.model.Playlist

class PlaylistsViewModel(application: Application) : AndroidViewModel(application) {

    // Use DataRepository instance to handle app data from Network or DB
    private val dataRepository = RepositoryFactory.getDataRepository()

    // MediatorLiveData can observe other LiveData objects and react on their emissions
    private var observablePlaylists: MediatorLiveData<List<Playlist>> = MediatorLiveData()
    private var observablePlaylistsMessage: MediatorLiveData<String> = MediatorLiveData()

    val playlists: LiveData<List<Playlist>>
        get() = observablePlaylists
    val playlistsName: LiveData<String>
        get() = observablePlaylistsMessage

    init {
        observablePlaylists.value = null
        observablePlaylists.addSource(dataRepository.getPlaylistsObservable()) {
            observablePlaylists.value = it
        }
        observablePlaylistsMessage.value = null
        observablePlaylistsMessage.addSource(dataRepository.getPlaylistsMessageObservable()) {
            observablePlaylistsMessage.value = it
        }
    }

    fun getPlaylists() {
        dataRepository.getFeaturedPlaylists()
    }
}