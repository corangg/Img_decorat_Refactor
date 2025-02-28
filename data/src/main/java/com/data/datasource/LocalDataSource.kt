package com.data.datasource

import com.data.datasource.local.room.LocalEmojiData
import com.data.datasource.local.room.LocalFontData
import com.data.datasource.local.room.LocalImageData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertImageData(entity: LocalImageData)
    fun getImageDataFlow(name: String = "temporary storage"): Flow<LocalImageData?>
    suspend fun getImageData(name: String = "temporary storage"): LocalImageData?
    suspend fun getImageDataList(): List<LocalImageData>
    suspend fun updateImageData(entity: LocalImageData)

    suspend fun insertEmojiData(entityList: List<LocalEmojiData>)
    fun getEmojiDataListFlow(): Flow<List<LocalEmojiData>>

    suspend fun insertFontData(entityList: List<LocalFontData>)
    fun getFontDataListFlow(): Flow<List<LocalFontData>>
}