package com.data.datasource.remote

import com.data.datasource.RemoteUnSplashDataSource
import javax.inject.Inject

class UnSplashRemoteDataSource @Inject constructor(
    private val unSplashApi: UnSplashApi
):RemoteUnSplashDataSource{
    override suspend fun getUnSplashList(keyword: String, key: String) = unSplashApi.getSearchedPhotos(query = keyword, apiKey = key)
}