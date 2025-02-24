package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.core.di.RemoteDataSources
import com.data.datasource.LocalDataSource
import com.data.datasource.RemoteEmojiDataSource
import com.data.datasource.RemoteUnSplashDataSource
import com.data.datasource.local.room.LocalViewItemData
import com.data.mapper.toExternal
import com.data.mapper.toLocal
import com.domain.model.ImageData
import com.domain.model.ViewItemData
import com.domain.repository.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultRepository @Inject constructor(
    @LocalDataSources private val localDataSource: LocalDataSource,
    @RemoteDataSources private val remoteUnSplashDataSource: RemoteUnSplashDataSource,
    @RemoteDataSources private val remoteEmojiDataSource: RemoteEmojiDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : Repository {
    override suspend fun insertImageData(imageData: ImageData) = withContext(ioDispatcher) {
        localDataSource.insertImageData(imageData.toLocal())
    }

    override suspend fun getImageData() = withContext(ioDispatcher) {
        localDataSource.getImageData()?.toExternal()
    }

    override suspend fun updateImageData(data: ImageData) = withContext(ioDispatcher) {
        localDataSource.updateImageData(data.toLocal())
    }

    override fun getTemporaryStorageImageDataFlow() =
        localDataSource.getImageDataFlow().map { it?.toExternal() }


    override suspend fun updateImageScale(scale: Float) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        localDataSource.updateImageData(imageData.copy(backgroundScale = scale))
    }

    override suspend fun updateImageBackgroundColor(color: Int) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        localDataSource.updateImageData(imageData.copy(backgroundColor = color))
    }

    override suspend fun getBackgroundImage(keyword: String) = withContext(ioDispatcher) {
        val key = "ccnpHuCKvzGXIlpTU8egOjDWKPNR2FTFIhb0hKjeZTY"
        val imageDataList = remoteUnSplashDataSource.getUnSplashList(keyword, key)
        imageDataList.map { it.remoteUrls.toExternal() }
    }

    override suspend fun updateBackgroundImage(url: String) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        localDataSource.updateImageData(imageData.copy(backgroundImage = url))
    }

    override suspend fun addViewItemData(data: ViewItemData) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        val viewItemData = imageData.viewDataInfo.toMutableList()
        viewItemData.add(data.toLocal())
        localDataSource.updateImageData(imageData.copy(viewDataInfo = viewItemData))
    }

    override suspend fun updateCheckImage(position: Int, visibility: Boolean) =
        withContext(ioDispatcher) {
            val imageData = localDataSource.getImageData() ?: return@withContext
            val imageList = imageData.viewDataInfo.toMutableList()
            imageList[position] = imageList[position].copy(visible = visibility)
            localDataSource.updateImageData(imageData.copy(viewDataInfo = imageList))
        }

    override suspend fun updateSwapImage(fromPos: Int, toPos: Int) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        val imageList = imageData.viewDataInfo.toMutableList()
        if (fromPos !in imageList.indices || toPos !in imageList.indices) return@withContext
        val item = imageList.removeAt(fromPos)
        imageList.add(toPos, item)
        localDataSource.updateImageData(imageData.copy(viewDataInfo = imageList))
    }

    override suspend fun updateSelectImage(position: Int) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        val imageList = imageData.viewDataInfo.toMutableList()
        val updatedList = imageList.mapIndexed { index, item ->
            item.copy(select = (index == position))
        }
        localDataSource.updateImageData(imageData.copy(viewDataInfo = updatedList))
    }

    override suspend fun deleteImage(position: Int) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        val imageList = imageData.viewDataInfo.toMutableList()
        if (position in imageList.indices) {
            imageList.removeAt(position)
            localDataSource.updateImageData(imageData.copy(viewDataInfo = imageList))
        }
    }

    override fun selectImageData() = flow {
        var previousPosition = -1
        localDataSource.getImageDataFlow().collect { data ->
            val newPosition = data?.viewDataInfo?.indexOfFirst { it.select } ?: return@collect
            if (previousPosition != newPosition) {
                previousPosition = newPosition
                emit(data.viewDataInfo[newPosition].toExternal())
            }
        }
    }

    override suspend fun updateImageSaturation(value: Float){
        updateImageData { it.copy(saturationValue = value) }
    }

    override suspend fun updateImageBrightness(value: Float) {
        updateImageData { it.copy(brightnessValue = value) }
    }

    override suspend fun updateImageTransparency(value: Float) {
        updateImageData { it.copy(transparencyValue = value) }
    }

    override suspend fun updateImageUri(uri: String) {
        updateImageData { it.copy(img = uri) }
    }

    private suspend fun updateImageData(update: (LocalViewItemData) -> LocalViewItemData) =
        withContext(ioDispatcher) {
            val imageData = localDataSource.getImageData() ?: return@withContext
            val imageList = imageData.viewDataInfo.toMutableList()
            val index = imageList.indexOfFirst { it.select }

            if (index != -1) {
                imageList[index] = update(imageList[index])
                localDataSource.updateImageData(imageData.copy(viewDataInfo = imageList))
            }
        }

    override suspend fun getEmoji() {
        val emojiKey = "d8764033dba5030b9399b02e869e4ee9b1a23211"
        val emojiList = remoteEmojiDataSource.getEmojiList(emojiKey)
        emojiList
        true
    }
}