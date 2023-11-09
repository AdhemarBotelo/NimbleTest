package com.adhemar.nimble.data.network.model

import com.adhemar.nimble.data.local.database.SurveyDB
import com.google.gson.annotations.SerializedName
import java.util.Date

data class SurveyMetaData(
    val page: Int,
    val pages: Int,
    @SerializedName("page_size") val pageSize: Int,
    val records: Int
)

data class SurveyResponse(
    val data: List<Survey>,
    val meta: SurveyMetaData
)

data class Survey(
    val id: String,
    val type: String,
    val attributes : SurveyAttribute,
)

data class SurveyAttribute(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("cover_image_url") val coverImageUrl: String,
    @SerializedName("survey_type") val surveyType: String
)

fun Survey.toSurveyDB() =
    SurveyDB(
        title = attributes.title,
        id = id,
        description = attributes.description,
        backgroundImageUrl = attributes.coverImageUrl
    )
