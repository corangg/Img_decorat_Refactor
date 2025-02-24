package com.data.di

import com.core.di.DefaultEmojiOkHttp
import com.core.di.EmojiRetrofitUseConvertGson
import com.core.di.EmojiUrl
import com.data.datasource.remote.EmojiApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmojiModule {
    @EmojiUrl
    @Provides
    fun provideEmojiUrl(): String = "https://emoji-api.com/"

    @EmojiRetrofitUseConvertGson
    @Provides
    @Singleton
    fun provideRetrofitUseConvertGsonServer(
        @EmojiUrl url: String,
        gson: Gson,
        @DefaultEmojiOkHttp client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @DefaultEmojiOkHttp
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideEmojiApi(@EmojiRetrofitUseConvertGson retrofit: Retrofit): EmojiApi =
        retrofit.create(EmojiApi::class.java)
}