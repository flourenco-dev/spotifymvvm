package com.fabiolourenco.spotifymvvm.data.model

import com.squareup.moshi.Json

data class Track(@field:Json(name = "id")           val id: String?,
                 @field:Json(name = "artists")      val artists: List<Artist>?,
                 @field:Json(name = "album")        val album: Album?,
                 @field:Json(name = "name")         val name: String?,
                 @field:Json(name = "is_playable")  val isPlayable: Boolean?,
                 @field:Json(name = "uri")          val uri: String?)