package com.adhemar.nimble.data

import com.adhemar.nimble.data.local.database.SurveyDB
import com.adhemar.nimble.data.local.database.SurveyDao
import com.adhemar.nimble.data.network.ApiResponse
import com.adhemar.nimble.data.network.ApiService
import com.adhemar.nimble.data.network.apiRequestFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val pageNumber = 1
const val pageSize = 10

interface ISurveyRepository {
    fun getSurveys(coroutineScope: CoroutineScope): Flow<ApiResponse<Boolean>>
    fun getSurveysFromDB(): Flow<List<SurveyDB>>
}

class SurveyRepository @Inject constructor(
    private val surveyDao: SurveyDao,
    private val surveyApi: ApiService
) : ISurveyRepository {

    override fun getSurveys(coroutineScope: CoroutineScope): Flow<ApiResponse<Boolean>> {
        return flow {
            coroutineScope.launch {
                apiRequestFlow {
                    surveyApi.getSurveys(pageNumber, pageSize)
                }.collect {
                    when (it) {
                        is ApiResponse.Failure -> {
                            println(it.errorMessage)
                            emit(ApiResponse.Failure(it.errorMessage, 400))
                        }

                        is ApiResponse.Loading -> {
                            emit(ApiResponse.Loading)
                        }

                        is ApiResponse.Success -> {
                            for (survey in it.data.data) {
                                surveyDao.insertSurvey(
                                    SurveyDB(
                                        title = survey.attributes.title,
                                        id = survey.id,
                                        description = survey.attributes.description,
                                        backgroundImageUrl = survey.attributes.coverImageUrl
                                    )
                                )
                            }
                            emit(ApiResponse.Success(true))
                        }
                    }
                }
            }
        }

    }

    override fun getSurveysFromDB(): Flow<List<SurveyDB>> {
        return surveyDao.getSurveys()
    }
}