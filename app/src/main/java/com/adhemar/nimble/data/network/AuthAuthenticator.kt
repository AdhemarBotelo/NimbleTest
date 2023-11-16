package com.adhemar.nimble.data.network

import com.adhemar.nimble.data.network.model.LogInResponse
import com.adhemar.nimble.data.network.model.RefreshTokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            tokenManager.getRefreshToken().first()
        }
        return runBlocking {
            val newToken = getNewToken(refreshToken)
            newToken.body()?.let {
                tokenManager.saveToken(it.data.attributes.accessToken)
                tokenManager.saveRefreshToken(it.data.attributes.refreshToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.data.attributes.accessToken}")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<LogInResponse> {
        val retrofit = Retrofit.Builder().baseUrl("https://jwt-test-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(AuthApiService::class.java)
        return service.refreshToken(
            refreshTokenRequest = RefreshTokenRequest(
                refreshToken = refreshToken ?: ""
            )
        )
    }


}