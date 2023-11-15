package com.adhemar.nimble.data.network.model

import com.adhemar.nimble.BuildConfig
import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("grant_type") val granType: String = GranType.Password.value,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("client_id") val clientId: String = BuildConfig.CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = BuildConfig.CLIENT_SECRET
)
