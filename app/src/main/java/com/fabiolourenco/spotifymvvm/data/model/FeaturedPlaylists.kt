package com.fabiolourenco.spotifymvvm.data.model

import com.squareup.moshi.Json

data class FeaturedPlaylists(@field:Json(name = "message")      val message: String?,
                             @field:Json(name = "playlists")    val playlists: PagingPlaylist?)