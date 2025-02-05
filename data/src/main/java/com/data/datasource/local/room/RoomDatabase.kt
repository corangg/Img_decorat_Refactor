package com.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalImageData::class], version = 1, exportSchema = false)

abstract class Database : RoomDatabase() {
    abstract fun imageDataDao(): ImageDataDao
}