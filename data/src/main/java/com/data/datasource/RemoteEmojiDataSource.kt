package com.data.datasource

import com.data.datasource.remote.RemoteEmojisData

interface RemoteEmojiDataSource {
    suspend fun getEmojiList(key: String): List<RemoteEmojisData>
}