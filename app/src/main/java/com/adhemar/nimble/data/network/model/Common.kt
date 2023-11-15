package com.adhemar.nimble.data.network.model

enum class GranType(val value:String) {
    Password("password"),
    RefreshToken("refresh_token")
}

data class ErrorResponse(
    val code:Int,
    val message:String
)