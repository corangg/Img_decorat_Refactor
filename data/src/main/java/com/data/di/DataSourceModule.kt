package com.data.di

import com.core.di.LocalDataSources
import com.core.di.RemoteDataSources
import com.data.datasource.LocalDataSource
import com.data.datasource.RemoteEmojiDataSource
import com.data.datasource.RemoteGoogleFontDataSource
import com.data.datasource.RemoteUnSplashDataSource
import com.data.datasource.local.DefaultLocalDataSource
import com.data.datasource.remote.EmojiRemoteDataSource
import com.data.datasource.remote.GoogleFontRemoteDataSource
import com.data.datasource.remote.UnSplashRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    @LocalDataSources
    abstract fun bindDefaultLocalDataSource(impl: DefaultLocalDataSource): LocalDataSource

    @Binds
    @Singleton
    @RemoteDataSources
    abstract fun bindDefaultRemoteUnsplashDataSource(impl: UnSplashRemoteDataSource): RemoteUnSplashDataSource

    @Binds
    @Singleton
    @RemoteDataSources
    abstract fun bindDefaultRemoteEmojiDataSource(impl: EmojiRemoteDataSource): RemoteEmojiDataSource

    @Binds
    @Singleton
    @RemoteDataSources
    abstract fun bindDefaultRemoteGoogleFontDataSource(impl: GoogleFontRemoteDataSource): RemoteGoogleFontDataSource
}