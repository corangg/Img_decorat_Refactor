package com.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.data.datasource.local.room.Database
import com.data.datasource.local.room.EmojiDataDao
import com.data.datasource.local.room.FontDao
import com.data.datasource.local.room.ImageDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE LocalEmojiData (emoji TEXT PRIMARY KEY NOT NULL, 'group' TEXT NOT NULL)"
            )
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE LocalFontData (fontName TEXT PRIMARY KEY NOT NULL, fontPath TEXT NOT NULL)"
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "Database.db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()

    @Provides
    fun provideImageDataDao(database: Database): ImageDataDao = database.imageDataDao()

    @Provides
    fun provideEmojiDataDao(database: Database): EmojiDataDao = database.emojiDataDao()

    @Provides
    fun provideFontDataDao(database: Database): FontDao = database.fontDataDao()
}