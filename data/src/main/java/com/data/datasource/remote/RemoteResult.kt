package com.data.datasource.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemoteUnsplashData(
    @Expose @SerializedName("urls") val remoteUrls: RemoteUrls,
)

data class RemoteUrls(
    @Expose @SerializedName("full") val full: String,
    @Expose @SerializedName("raw") val raw: String,
    @Expose @SerializedName("regular") val regular: String,
    @Expose @SerializedName("small") val small: String,
    @Expose @SerializedName("thumb") val thumb: String
)

data class RemoteEmojisData(
    @Expose @SerializedName("character") val character: String,
    @Expose @SerializedName("group") val group: String,
)

data class RemoteGoogleFontsData(
    @Expose @SerializedName("items") val fonts: List<FontItem>
)

data class FontItem(
    @Expose @SerializedName("family") val family: String,
    @Expose @SerializedName("category") val category: String,
    @Expose @SerializedName("variants") val variants: List<String>,
    @Expose @SerializedName("files") val files: Map<String, String>
)