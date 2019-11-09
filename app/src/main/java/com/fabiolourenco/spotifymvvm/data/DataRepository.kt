package com.fabiolourenco.spotifymvvm.data

import androidx.lifecycle.LiveData
import com.fabiolourenco.spotifymvvm.data.model.Playlist
import com.fabiolourenco.spotifymvvm.data.model.Track

interface DataRepository {
    fun getPlaylistsObservable(): LiveData<List<Playlist>>
    fun getPlaylistsMessageObservable(): LiveData<String>

    fun getFeaturedPlaylists()
    fun getPlaylistTracks(playlistId: String): LiveData<List<Track>>
//    fun getPlaylistTracks(playlistId: String, limit: Int, offset: Int)

    fun setToken(token: String, expiresIn: Int)
}