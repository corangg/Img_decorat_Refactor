package com.data.datasource

import com.data.datasource.remote.RemoteGoogleFontsData

interface RemoteGoogleFontDataSource {
    suspend fun getGoogleFontList(key: String): RemoteGoogleFontsData
}