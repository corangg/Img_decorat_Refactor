package com.data.datasource.remote

import com.data.datasource.RemoteGoogleFontDataSource
import javax.inject.Inject

class GoogleFontRemoteDataSource @Inject constructor(
    private val googleFontApi: GoogleFontApi
) : RemoteGoogleFontDataSource {
    override suspend fun getGoogleFontList(key: String) = googleFontApi.getFonts(key)
}