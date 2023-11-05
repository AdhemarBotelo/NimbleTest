package com.adhemar.nimble.data.network.model

enum class GranType(val value:String) {
    Password("password"),
    RefreshToken("refresh_token")
}

const val CLIENT_ID = "ofzl-2h5ympKa0WqqTzqlVJUiRsxmXQmt5tkgrlWnOE"
const val CLIENT_SECRET= "lMQb900L-mTeU-FVTCwyhjsfBwRCxwwbCitPob96cuU"

data class ErrorResponse(
    val code:String,
    val detail:String
)