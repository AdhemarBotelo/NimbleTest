package com.adhemar.nimble.data

import com.adhemar.nimble.data.local.database.SurveyDB
import com.adhemar.nimble.data.local.database.SurveyDao
import com.adhemar.nimble.data.network.ApiService
import com.adhemar.nimble.data.network.model.toSurveyDB
import javax.inject.Inject

const val pageNumber = 1
const val pageSize = 10

interface ISurveyRepository {
    suspend fun getSurveys(): List<SurveyDB>
    suspend fun getSurveysFromDB(): List<SurveyDB>
}

class SurveyRepository @Inject constructor(
    private val surveyDao: SurveyDao,
    private val surveyApi: ApiService
) : ISurveyRepository {

    override suspend fun getSurveys(): List<SurveyDB> {
        var response = emptyList<SurveyDB>()
        val call = surveyApi.getSurveys(pageNumber, pageSize)
        if (call.isSuccessful) {
            surveyDao.deleteAllSurvey()
            response = call.body()?.data?.map { it.toSurveyDB() } ?: emptyList()
            surveyDao.insertSurvey(response)
        } else {
            surveyDao.getSurveys()
        }
        return response
    }

    override suspend fun getSurveysFromDB(): List<SurveyDB> {
        return surveyDao.getSurveys()
    }
}