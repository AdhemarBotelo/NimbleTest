package com.adhemar.nimble.data.network

import com.adhemar.nimble.data.network.model.LogInResponse
import com.adhemar.nimble.data.network.model.LoginRequest
import com.adhemar.nimble.data.network.model.RefreshTokenRequest
import com.adhemar.nimble.data.network.model.SurveyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("oauth/token")
    suspend fun logIn(@Body loginRequest: LoginRequest):Response<LogInResponse>

    @POST("oauth/token")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest):Response<LogInResponse>

    @GET("surveys")
    suspend fun getSurveys():Response<SurveyResponse>

}