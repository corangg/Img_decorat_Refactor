package com.data.datasource

import com.data.datasource.remote.RemoteUnsplashData

interface RemoteUnSplashDataSource {
    suspend fun getUnSplashList(keyword: String, key: String): List<RemoteUnsplashData>
}