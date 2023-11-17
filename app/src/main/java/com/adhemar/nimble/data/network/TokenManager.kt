package com.adhemar.nimble.data.network

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.adhemar.nimble.data.CryptoManager
import com.adhemar.nimble.data.di.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject


const val TOKEN_FILE = "token.txt"
const val REFRESH_TOKEN_FILE = "token2.txt"

class TokenManager @Inject constructor(private val context: Context) : ITokenManager {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
        private val IS_LOGGED_KEY = stringPreferencesKey("is_logged key")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    val cryptoManager = CryptoManager()


    override fun getToken(): Flow<String?> {
        val file = File(context.filesDir, TOKEN_FILE)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flow {
                if (file.exists()) {
                    emit(
                        cryptoManager.decrypt(
                            inputStream = FileInputStream(file)
                        )
                            .decodeToString()
                    )
                } else {
                    emit("")
                }

            }
        } else {
            context.dataStore.data.map { preferences ->
                preferences[TOKEN_KEY]
            }
        }
    }

    override suspend fun saveToken(token: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bytes = token.encodeToByteArray()
            val file = File(context.filesDir, TOKEN_FILE)
            if (!file.exists()) {
                file.createNewFile()
            }
            val fos = FileOutputStream(file)

            cryptoManager.encrypt(
                bytes = bytes, outputStream = fos
            ).decodeToString()
        } else {
            context.dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = token
            }
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        val file = File(context.filesDir, REFRESH_TOKEN_FILE)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flow {
                if (file.exists()) {
                    emit(
                        cryptoManager.decrypt(
                            inputStream = FileInputStream(file)
                        ).decodeToString()
                    )
                } else {
                    emit("")
                }
            }
        } else {
            context.dataStore.data.map { preferences ->
                preferences[REFRESH_TOKEN_KEY]
            }
        }
    }

    override suspend fun saveRefreshToken(token: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bytes = token.encodeToByteArray()
            val file = File(context.filesDir, REFRESH_TOKEN_FILE)
            if (!file.exists()) {
                file.createNewFile()
                val fos = FileOutputStream(file)
                cryptoManager.encrypt(
                    bytes = bytes, outputStream = fos
                ).decodeToString()
            }
        } else {
            context.dataStore.edit { preferences ->
                preferences[REFRESH_TOKEN_KEY] = token
            }
        }
    }

    override suspend fun saveLoginStatus(isLogged: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_KEY] = isLogged.toString()
        }
    }

    override suspend fun getLoginStatus(): Flow<Boolean> {
        return context.dataStore.data.map { preference ->
            preference[IS_LOGGED_KEY].toBoolean()
        }
    }

    override suspend fun deleteTokens() {
        val file = File(context.filesDir, TOKEN_FILE)
        val fileRefresh = File(context.filesDir, TOKEN_FILE)
        file.deleteOnExit()
        fileRefresh.deleteOnExit()
    }
}