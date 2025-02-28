package com.data.datasource.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleFontApi {
    @GET("v1/webfonts")
    suspend fun getFonts(
        @Query("key") apiKey: String,
        @Query("sort") sort: String = "popularity"
    ): RemoteGoogleFontsData
}