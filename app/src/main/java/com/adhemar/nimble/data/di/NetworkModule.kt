package com.adhemar.nimble.data.di

import com.adhemar.nimble.data.network.ApiService
import com.adhemar.nimble.data.network.AuthApiService
import com.adhemar.nimble.data.network.AuthAuthenticator
import com.adhemar.nimble.data.network.AuthInterceptor
import com.adhemar.nimble.data.network.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://survey-api.nimblehq.co/api/v1/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator=
        AuthAuthenticator(tokenManager)
    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Provides
    fun provideRetrofitBuilder(
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    }

    @Singleton
    @Provides
    fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthApiService =
        retrofit
            .build()
            .create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideMainAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): ApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
}