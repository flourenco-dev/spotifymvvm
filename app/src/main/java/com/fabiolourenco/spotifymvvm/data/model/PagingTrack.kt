package com.fabiolourenco.spotifymvvm.data.model

import com.squareup.moshi.Json

data class PagingTrack(@field:Json(name = "items")  val items: List<ItemTrack>?,
                       @field:Json(name = "limit")  val limit: Int?,
                       @field:Json(name = "offset") val offset: Int?,
                       @field:Json(name = "total")  val total: Int?)