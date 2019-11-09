package com.fabiolourenco.spotifymvvm.ui.tracks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.fabiolourenco.spotifymvvm.data.RepositoryFactory
import com.fabiolourenco.spotifymvvm.data.model.Track

class TracksViewModel(application: Application) : AndroidViewModel(application) {

    // Use DataRepository instance to handle app data from Network or DB
    private val dataRepository = RepositoryFactory.getDataRepository()

    // MediatorLiveData can observe other LiveData objects and react on their emissions
    private var observableTracks: MediatorLiveData<List<Track>> = MediatorLiveData()

    val tracks: LiveData<List<Track>>
        get() = observableTracks

    init {
        observableTracks.value = null
    }

    fun getTracks(playlistId: String) {
        observableTracks.addSource(dataRepository.getPlaylistTracks(playlistId)) {
            observableTracks.value = it
        }
    }
}