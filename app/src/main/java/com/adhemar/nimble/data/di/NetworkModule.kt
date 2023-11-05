package com.adhemar.nimble.data.di

import com.adhemar.nimble.data.network.ApiService
import com.adhemar.nimble.data.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val BASE_URL = "https://survey-api.nimblehq.co/api/v1/"
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(apiService = retrofit.create(ApiService::class.java)))
            .build()

        return retrofit

    }
}