package com.fabiolourenco.spotifymvvm.data.api

import com.fabiolourenco.spotifymvvm.data.model.FeaturedPlaylists
import com.fabiolourenco.spotifymvvm.data.model.PagingTrack
import retrofit2.Call
import retrofit2.http.*

interface SpotifyApi {

    @GET("browse/featured-playlists?country=PT")
    fun getFeaturedPlaylists(@HeaderMap headers: Map<String, String>): Call<FeaturedPlaylists>

    @GET("playlists/{playlistId}/tracks")
    fun getPlaylistTracks(@HeaderMap headers: Map<String, String>,
                          @Path(value = "playlistId") playlistId: String): Call<PagingTrack>

    @GET("playlists/{playlistId}/tracks")
    fun getPlaylistTracks(@HeaderMap headers: Map<String, String>,
                          @Path(value = "playlistId") playlistId: String,
                          @Query(value = "limit") limit: Int,
                          @Query(value = "offset") offset: Int): Call<PagingTrack>
}