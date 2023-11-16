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
    }

    @RequiresApi(Build.VERSION_CODES.M)
    val cryptoManager = CryptoManager()


    override fun getToken(): Flow<String?> {
        val file = File(context.filesDir, TOKEN_FILE)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flow {
                emit(
                    withContext(Dispatchers.IO) {
                        cryptoManager.decrypt(
                            inputStream = FileInputStream(file)
                        )
                    }.decodeToString()
                )
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
                withContext(Dispatchers.IO) {
                    file.createNewFile()
                }
            }
            val fos = withContext(Dispatchers.IO) {
                FileOutputStream(file)
            }
            cryptoManager.encrypt(
                bytes = bytes,
                outputStream = fos
            ).decodeToString()
        } else {
            context.dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = token
            }
        }
    }

    override suspend fun deleteToken() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val file = File(context.filesDir, TOKEN_FILE)
            file.delete()
        } else {
            context.dataStore.edit { preferences ->
                preferences.remove(TOKEN_KEY)
            }
        }
    }

    override fun getRefreshToken(): Flow<String?> {
        val file = File(context.filesDir, REFRESH_TOKEN_FILE)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flow {
                emit(
                    withContext(Dispatchers.IO) {
                        cryptoManager.decrypt(
                            inputStream = FileInputStream(file)
                        )
                    }.decodeToString()
                )
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
                withContext(Dispatchers.IO) {
                    file.createNewFile()
                }
                val fos = withContext(Dispatchers.IO) {
                    FileOutputStream(file)
                }
                cryptoManager.encrypt(
                    bytes = bytes,
                    outputStream = fos
                ).decodeToString()
            }
        } else {
            context.dataStore.edit { preferences ->
                preferences[REFRESH_TOKEN_KEY] = token
            }
        }
    }

    override suspend fun deleteRefreshToken() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val file = File(context.filesDir, REFRESH_TOKEN_FILE)
            file.delete()
        } else {
            context.dataStore.edit { preferences ->
                preferences.remove(REFRESH_TOKEN_KEY)
            }
        }
    }
}