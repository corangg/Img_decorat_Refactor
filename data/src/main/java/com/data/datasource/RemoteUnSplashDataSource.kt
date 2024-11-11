package com.data.datasource

import com.data.datasource.remote.LocalUnsplashData

interface RemoteUnSplashDataSource {
    suspend fun getUnSplashList(keyword: String, key: String): List<LocalUnsplashData>
}