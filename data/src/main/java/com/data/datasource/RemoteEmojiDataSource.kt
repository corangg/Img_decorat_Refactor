package com.data.datasource

import com.data.datasource.remote.RemoteEmojis

interface RemoteEmojiDataSource {
    suspend fun getEmojiList(key: String): List<RemoteEmojis>
}