package com.adhemar.nimble.data.network

import kotlinx.coroutines.flow.Flow

interface ITokenManager {
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)

    fun getRefreshToken(): Flow<String?>
    suspend fun saveRefreshToken(token: String)
    suspend fun saveLoginStatus(isLogged: Boolean)
    suspend fun getLoginStatus(): Flow<Boolean>
    suspend fun deleteTokens()
}