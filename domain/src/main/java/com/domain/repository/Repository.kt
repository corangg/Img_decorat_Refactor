package com.domain.repository

import com.domain.model.ImageData
import com.domain.model.UnSplashUrls
import com.domain.model.ViewItemData
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun insertImageData(imageData: ImageData)

    suspend fun getImageData(): ImageData?

    fun getTemporaryStorageImageDataFlow(): Flow<ImageData?>

    suspend fun updateImageData(data: ImageData)

    suspend fun updateImageScale(scale: Float)

    suspend fun updateImageBackgroundColor(color: Int)

    suspend fun getBackgroundImage(keyword: String): List<UnSplashUrls>

    suspend fun updateBackgroundImage(url: String)

    suspend fun addViewItemData(data: ViewItemData)

    suspend fun updateCheckImage(position: Int, visibility: Boolean)

    suspend fun updateSwapImage(fromPos: Int, toPos: Int)

    suspend fun deleteImage(position: Int)
}