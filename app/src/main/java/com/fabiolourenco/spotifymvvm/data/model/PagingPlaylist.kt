package com.fabiolourenco.spotifymvvm.data.model

import com.squareup.moshi.Json

data class PagingPlaylist(@field:Json(name = "items") val items: List<Playlist>?,
                          @field:Json(name = "total") val total: Int?)