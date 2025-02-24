package com.data.datasource.remote

import com.data.datasource.RemoteEmojiDataSource
import javax.inject.Inject

class EmojiRemoteDataSource @Inject constructor(
    private val emojiApi: EmojiApi
): RemoteEmojiDataSource {
    override suspend fun getEmojiList(key: String): List<RemoteEmojisData> = emojiApi.getEmojis(key)
}