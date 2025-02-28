package com.data.di

import com.core.di.DefaultGoogleFontOkHttp
import com.core.di.GoogleFontRetrofitUseConvertGson
import com.core.di.GoogleFontUrl
import com.data.datasource.remote.GoogleFontApi
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
object GoogleFontModule {
    @GoogleFontUrl
    @Provides
    fun provideGoogleFontUrl(): String = "https://www.googleapis.com/webfonts/"

    @GoogleFontRetrofitUseConvertGson
    @Provides
    @Singleton
    fun provideRetrofitUseConvertGsonServer(
        @GoogleFontUrl url: String,
        gson: Gson,
        @DefaultGoogleFontOkHttp client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @DefaultGoogleFontOkHttp
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
    fun provideGoogleFontApi(@GoogleFontRetrofitUseConvertGson retrofit: Retrofit): GoogleFontApi =
        retrofit.create(GoogleFontApi::class.java)
}