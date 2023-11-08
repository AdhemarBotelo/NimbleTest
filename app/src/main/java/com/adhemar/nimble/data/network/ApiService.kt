package com.adhemar.nimble.data.network

import com.adhemar.nimble.data.network.model.LogInResponse
import com.adhemar.nimble.data.network.model.LoginRequest
import com.adhemar.nimble.data.network.model.RefreshTokenRequest
import com.adhemar.nimble.data.network.model.SurveyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("surveys")
    suspend fun getSurveys(
        @Query("page[number]") pageNumber:Int,
        @Query("page[size]") pageSize:Int

    ):Response<SurveyResponse>

}