package com.domain.repository

import android.view.View
import com.domain.model.EmojiData
import com.domain.model.ImageData
import com.domain.model.UnSplashData
import com.domain.model.ViewItemData
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun insertImageData(imageData: ImageData)

    suspend fun getImageData(): ImageData?

    fun getTemporaryStorageImageDataFlow(): Flow<ImageData?>

    suspend fun updateImageData(data: ImageData)

    suspend fun updateImageScale(scale: Float)

    suspend fun updateImageBackgroundColor(color: Int)

    suspend fun getBackgroundImage(keyword: String): List<UnSplashData>

    suspend fun updateBackgroundImage(url: String)

    suspend fun addViewItemData(data: ViewItemData)

    suspend fun updateCheckImage(position: Int, visibility: Boolean)

    suspend fun updateSwapImage(fromPos: Int, toPos: Int)

    suspend fun updateSelectImage(position: Int)

    suspend fun deleteImage(position: Int)

    fun selectImageData(): Flow<ViewItemData>

    suspend fun updateImageSaturation(value: Float)

    suspend fun updateImageBrightness(value: Float)

    suspend fun updateImageTransparency(value: Float)

    suspend fun updateImageUri(uri: String)

    suspend fun downloadEmoji()

    fun getEmoji(): Flow<List<EmojiData>>

    suspend fun updateTextSize(size: Int)

    suspend fun updateTextColor(color: Int)

    suspend fun updateTextBackgroundColor(color: Int)

    suspend fun updateTextFont(fontPath: String)

    suspend fun downloadGoogleFontList()

    fun getFontPath(): Flow<List<String>>

    suspend fun extractFile(view: View)
}