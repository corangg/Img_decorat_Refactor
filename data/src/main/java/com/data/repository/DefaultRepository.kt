package com.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Environment
import android.view.View
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.core.di.RemoteDataSources
import com.core.util.getLocalTimeToString
import com.data.datasource.LocalDataSource
import com.data.datasource.RemoteEmojiDataSource
import com.data.datasource.RemoteGoogleFontDataSource
import com.data.datasource.RemoteUnSplashDataSource
import com.data.datasource.local.room.LocalViewItemData
import com.data.mapper.toExternal
import com.data.mapper.toExternalList
import com.data.mapper.toLocal
import com.domain.model.ImageData
import com.domain.model.ViewItemData
import com.domain.repository.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class DefaultRepository @Inject constructor(
    @LocalDataSources private val localDataSource: LocalDataSource,
    @RemoteDataSources private val remoteUnSplashDataSource: RemoteUnSplashDataSource,
    @RemoteDataSources private val remoteEmojiDataSource: RemoteEmojiDataSource,
    @RemoteDataSources private val remoteGoogleFontDataSource: RemoteGoogleFontDataSource,
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

    override suspend fun updateImageSaturation(value: Float) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(saturationValue = value) }
    }

    override suspend fun updateImageBrightness(value: Float) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(brightnessValue = value) }
    }

    override suspend fun updateImageTransparency(value: Float) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(transparencyValue = value) }
    }

    override suspend fun updateImageUri(uri: String) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(img = uri) }
    }

    override suspend fun downloadEmoji() {
        val emojiKey = "d8764033dba5030b9399b02e869e4ee9b1a23211"
        val emojiList = remoteEmojiDataSource.getEmojiList(emojiKey)
        localDataSource.insertEmojiData(emojiList.map { it.toLocal() })
    }

    override fun getEmoji() = localDataSource.getEmojiDataListFlow().toExternalList()

    override suspend fun updateTextSize(size: Int) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(textSize = size) }
    }

    override suspend fun updateTextColor(color: Int) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(textColor = color) }
    }

    override suspend fun updateTextBackgroundColor(color: Int) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(textBackGroundColor = color) }
    }

    override suspend fun updateTextFont(fontPath: String) = withContext(ioDispatcher) {
        updateImageProperty { it.copy(font = fontPath) }
    }

    private suspend fun updateImageProperty(
        updateAction: (LocalViewItemData) -> LocalViewItemData
    ) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        val imageList = imageData.viewDataInfo.toMutableList()
        val index = imageList.indexOfFirst { it.select }
        if (index != -1) {
            updateAction(imageList[index])
            imageList[index] = updateAction(imageList[index])
            localDataSource.updateImageData(imageData.copy(viewDataInfo = imageList))
        }
    }

    override suspend fun downloadGoogleFontList() = withContext(ioDispatcher) {
        val key = "AIzaSyDQR_0ir3bcSscuBdAR-TUUxGcoLethfV0"
        val fontDataList = remoteGoogleFontDataSource.getGoogleFontList(key).fonts.take(30)
        val fontPathList = fontDataList.mapNotNull { it.toLocal(context) }

        localDataSource.insertFontData(fontPathList)
    }

    override fun getFontPath(): Flow<List<String>> {
        return localDataSource.getFontDataListFlow().map { fontData ->
            fontData.map { it.fontPath }
        }
    }

    override suspend fun extractFile(view: View) = withContext(ioDispatcher) {
        clearSelectBorder()
        val bitmap = getBitmapFromView(view)
        saveBitmapToFile(bitmap)
    }

    private suspend fun clearSelectBorder(){
        val imageData = localDataSource.getImageData() ?: return
        val imageList = imageData.viewDataInfo.toMutableList()
        val updatedList = imageList.map {
            it.copy(select = false)
        }
        localDataSource.updateImageData(imageData.copy(viewDataInfo = updatedList))
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap) {
        val externalStorage =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val folder = File(externalStorage, "IKKU")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val localTime = getLocalTimeToString()

        var file = File(folder, "$localTime.png")
        var fileIndex = 1
        while (file.exists()) {
            file = File(folder, "${localTime}_$fileIndex.png")
            fileIndex++
        }
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}