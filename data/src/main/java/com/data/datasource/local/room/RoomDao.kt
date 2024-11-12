package com.data.datasource.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import com.data.datasource.local.room.converter.ImageDataConverter
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(ImageDataConverter::class)
interface ImageDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageData(entity: LocalImageData)

    @Query("SELECT * FROM LocalImageData WHERE name = :name")
    fun getImageDataFlow(name: String): Flow<LocalImageData?>

    @Query("SELECT * FROM LocalImageData WHERE name = :name")
    fun getImageData(name: String): LocalImageData?

    @Query("SELECT * FROM LocalImageData")
    suspend fun getImageDataList(): List<LocalImageData>

    @Update
    suspend fun updateImageData(entity: LocalImageData)
}