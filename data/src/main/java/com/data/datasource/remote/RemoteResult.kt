package com.data.datasource.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocalUnsplashData(
    @Expose @SerializedName("urls") val localUrls: LocalUrls,
)

data class LocalUrls(
    @Expose @SerializedName("full") val full: String,
    @Expose @SerializedName("raw") val raw: String,
    @Expose @SerializedName("regular") val regular: String,
    @Expose @SerializedName("small") val small: String,
    @Expose @SerializedName("thumb") val thumb: String
)
