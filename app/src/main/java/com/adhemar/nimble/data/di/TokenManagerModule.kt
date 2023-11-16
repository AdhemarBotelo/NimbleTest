package com.adhemar.nimble.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.adhemar.nimble.data.network.ITokenManager
import com.adhemar.nimble.data.network.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenManagerModule {
    @Singleton
    @Binds
    abstract fun bindTokenManager(
        tokenManager: TokenManager
    ): ITokenManager
}

class FakeTokenManager @Inject constructor(private val context: Context) : ITokenManager {

    override fun getToken(): Flow<String?> {
        return flow {
            emit("123412341234")
        }
    }

    override suspend fun saveToken(token: String) {

    }

    override suspend fun deleteToken() {

    }

    override fun getRefreshToken(): Flow<String?> {
        return flow {
            emit("123412341234")
        }
    }

    override suspend fun saveRefreshToken(token: String) {

    }

    override suspend fun deleteRefreshToken() {

    }

}