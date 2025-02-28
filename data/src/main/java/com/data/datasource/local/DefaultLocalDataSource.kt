package com.data.datasource.local

import com.data.datasource.LocalDataSource
import com.data.datasource.local.room.EmojiDataDao
import com.data.datasource.local.room.FontDao
import com.data.datasource.local.room.ImageDataDao
import com.data.datasource.local.room.LocalEmojiData
import com.data.datasource.local.room.LocalFontData
import com.data.datasource.local.room.LocalImageData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalDataSource @Inject constructor(
    private val imageDataDao: ImageDataDao,
    private val emojiDataDao: EmojiDataDao,
    private val fontDataDao: FontDao
) : LocalDataSource {
    override suspend fun insertImageData(entity: LocalImageData) = imageDataDao.insertImageData(entity)

    override suspend fun getImageData(name: String): LocalImageData? = imageDataDao.getImageData(name)

    override fun getImageDataFlow(name: String) = imageDataDao.getImageDataFlow(name)

    override suspend fun getImageDataList() = imageDataDao.getImageDataList()

    override suspend fun updateImageData(entity: LocalImageData) = imageDataDao.updateImageData(entity)

    override suspend fun insertEmojiData(entityList: List<LocalEmojiData>) = emojiDataDao.insertEmojiData(entityList)

    override fun getEmojiDataListFlow(): Flow<List<LocalEmojiData>> = emojiDataDao.getEmojiDataList()

    override suspend fun insertFontData(entityList: List<LocalFontData>) = fontDataDao.insertFontData(entityList)

    override fun getFontDataListFlow() = fontDataDao.getFontDataList()
}