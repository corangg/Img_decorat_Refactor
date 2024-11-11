package com.data.di

import com.data.datasource.remote.UnSplashApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnSplashUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnSplashRetrofitUseConvertGson

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultUnSplashOkHttp

@Module
@InstallIn(SingletonComponent::class)
object ServerModule {
    @UnSplashUrl
    @Provides
    fun provideUnSplashUrl(): String = "https://api.unsplash.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @UnSplashRetrofitUseConvertGson
    @Provides
    @Singleton
    fun provideRetrofitUseConvertGsonServer(
        @UnSplashUrl url: String,
        gson: Gson,
        @DefaultUnSplashOkHttp client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @DefaultUnSplashOkHttp
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
    fun provideUnSplashApi(@UnSplashRetrofitUseConvertGson retrofit: Retrofit): UnSplashApi =
        retrofit.create(UnSplashApi::class.java)
}