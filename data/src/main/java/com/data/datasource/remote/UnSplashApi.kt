package com.data.datasource.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnSplashApi {
    @GET("photos/random")
    suspend fun getSearchedPhotos(
        @Query("client_id") apiKey: String,  // API 키를 쿼리로 전달
        @Query("count") count: Int = 20,  // 개수를 쿼리로 설정, 기본값 20
        @Query("query") query: String
    ): List<LocalUnsplashData>
}