package com.data.datasource.local

import com.data.datasource.LocalDataSource
import com.data.datasource.local.room.ImageDataDao
import com.data.datasource.local.room.LocalImageData
import javax.inject.Inject

class DefaultLocalDataSource @Inject constructor(
    private val imageDataDao: ImageDataDao
) : LocalDataSource {
    override suspend fun insertImageData(entity: LocalImageData) = imageDataDao.insertImageData(entity)

    override suspend fun getImageData(name: String): LocalImageData? = imageDataDao.getImageData(name)

    override fun getImageDataFlow(name: String) = imageDataDao.getImageDataFlow(name)

    override suspend fun getImageDataList() = imageDataDao.getImageDataList()

    override suspend fun updateImageData(entity: LocalImageData) = imageDataDao.updateImageData(entity)
}