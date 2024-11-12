package com.data.datasource.local.room.converter

import androidx.room.TypeConverter
import com.data.datasource.local.room.LocalViewItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageDataConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMatrixValues(matrixValues: Array<Float>): String {
        return gson.toJson(matrixValues)
    }

    @TypeConverter
    fun toMatrixValues(data: String): Array<Float> {
        val listType = object : TypeToken<Array<Float>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromSaveViewDataInfoList(data: List<LocalViewItemData>): String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toSaveViewDataInfoList(data: String): List<LocalViewItemData> {
        val listType = object : TypeToken<List<LocalViewItemData>>() {}.type
        return gson.fromJson(data, listType)
    }
}