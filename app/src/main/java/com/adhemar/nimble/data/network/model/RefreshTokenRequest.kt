package com.adhemar.nimble.data.network.model

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("grant_type") val granType: GranType = GranType.RefreshToken,
    @SerializedName("refresh_token") val refreshToken:String,
    @SerializedName("client_id") val clientId:String = CLIENT_ID,
    @SerializedName("client_secret") val clientSecret:String = CLIENT_SECRET
)