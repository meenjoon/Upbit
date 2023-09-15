package com.mbj.upbit.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinInfo(
    @Json(name = "market") val market: String,
    @Json(name = "korean_name") val koreanName: String,
    @Json(name = "english_name") val englishName: String
)
