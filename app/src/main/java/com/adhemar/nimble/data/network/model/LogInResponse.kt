package com.adhemar.nimble.data.network.model

import com.google.gson.annotations.SerializedName

data class LogInResponse(val data: OauthToken)

data class OauthToken(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type:String,
    @SerializedName("attributes") val attributes:OauthTokenAtribute

)
data class OauthTokenAtribute(
    @SerializedName("access_token") val accessToken:String,
    @SerializedName("token_type") val tokenType:String,
    @SerializedName("expires_in") val expiresIn:Long,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("created_at") val createdAt : Long
)

