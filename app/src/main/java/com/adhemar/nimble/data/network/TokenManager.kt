package com.adhemar.nimble.data.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.adhemar.nimble.data.di.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManager @Inject constructor(private val context: Context) : ITokenManager {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
    }

    override fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun deleteToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }

    override suspend fun deleteRefreshToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}