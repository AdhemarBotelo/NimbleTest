package com.adhemar.nimble.data.network.model

import com.adhemar.nimble.BuildConfig
import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("grant_type") val granType: String = GranType.RefreshToken.value,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("client_id") val clientId: String = BuildConfig.CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = BuildConfig.CLIENT_SECRET
)