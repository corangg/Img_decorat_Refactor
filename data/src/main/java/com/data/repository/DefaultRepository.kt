package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.core.di.RemoteDataSources
import com.data.datasource.LocalDataSource
import com.data.datasource.RemoteUnSplashDataSource
import com.data.mapper.toExternal
import com.data.mapper.toLocal
import com.domain.model.ImageData
import com.domain.repository.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultRepository @Inject constructor(
    @LocalDataSources private val localDataSource: LocalDataSource,
    @RemoteDataSources private val remoteUnSplashDataSource: RemoteUnSplashDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : Repository {
    override suspend fun insertImageData(imageData: ImageData) = withContext(ioDispatcher) {
        localDataSource.insertImageData(imageData.toLocal())
    }

    override suspend fun getImageData() = withContext(ioDispatcher) {
        localDataSource.getImageData()?.toExternal()
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
        imageDataList.map { it.localUrls.toExternal() }
    }

    override suspend fun updateBackgroundImage(url: String) = withContext(ioDispatcher) {
        val imageData = localDataSource.getImageData() ?: return@withContext
        localDataSource.updateImageData(imageData.copy(backgroundImage = url))
    }
}