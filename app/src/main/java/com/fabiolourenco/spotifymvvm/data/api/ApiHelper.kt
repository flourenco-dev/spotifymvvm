package com.fabiolourenco.spotifymvvm.data.api

import com.fabiolourenco.spotifymvvm.data.model.FeaturedPlaylists
import com.fabiolourenco.spotifymvvm.data.model.PagingTrack
import retrofit2.Call

interface ApiHelper {
    fun getFeaturedPlaylists(): Call<FeaturedPlaylists>
    fun getPlaylistTracks(playlistId: String): Call<PagingTrack>
    fun getPlaylistTracks(playlistId: String, limit: Int, offset: Int): Call<PagingTrack>

    fun setToken(token: String, expiresIn: Int)
}