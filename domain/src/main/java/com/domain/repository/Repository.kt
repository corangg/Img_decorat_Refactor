package com.domain.repository

import com.domain.model.ImageData
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun insertImageData(imageData: ImageData)

    suspend fun getImageData(): ImageData?

    fun getTemporaryStorageImageDataFlow(): Flow<ImageData?>

    suspend fun updateImageScale(scale: Float)

    suspend fun updateImageBackgroundColor(color: Int)
}