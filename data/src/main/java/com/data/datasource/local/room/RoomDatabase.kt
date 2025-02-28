package com.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalImageData::class, LocalEmojiData::class, LocalFontData::class],
    version = 3,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun imageDataDao(): ImageDataDao
    abstract fun emojiDataDao(): EmojiDataDao
    abstract fun fontDataDao(): FontDao
}