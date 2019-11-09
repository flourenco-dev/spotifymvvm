package com.fabiolourenco.spotifymvvm.data.model

import com.squareup.moshi.Json

data class Artist(@field:Json(name = "id")        val id: String?,
                  @field:Json(name = "name")      val name: String?,
                  @field:Json(name = "uri")       val uri: String?)