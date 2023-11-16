package com.adhemar.nimble.data.network

import kotlinx.coroutines.flow.Flow

interface ITokenManager {
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun deleteToken()
    fun getRefreshToken(): Flow<String?>
    suspend fun saveRefreshToken(token: String)
    suspend fun deleteRefreshToken()
}