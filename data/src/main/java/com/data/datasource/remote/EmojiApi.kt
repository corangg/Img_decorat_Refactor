package com.data.datasource.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface EmojiApi {
    @GET("emojis")
    suspend fun getEmojis(
        @Query("access_key") apiKey: String
    ): List<RemoteEmojis>
}