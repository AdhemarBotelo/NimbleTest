package com.adhemar.nimble.data

import com.adhemar.nimble.data.local.database.SurveyDao
import com.adhemar.nimble.data.network.ApiService
import com.adhemar.nimble.data.network.model.Survey
import com.adhemar.nimble.data.network.model.SurveyAttribute
import com.adhemar.nimble.data.network.model.SurveyMetaData
import com.adhemar.nimble.data.network.model.SurveyResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class SurveyRepositoryTest {
    @RelaxedMockK
    private lateinit var surveyDao: SurveyDao

    @RelaxedMockK
    private lateinit var surveyApi: ApiService
    private lateinit var surveyRepository: SurveyRepository

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        surveyRepository = SurveyRepository(surveyDao, surveyApi = surveyApi)
    }

    @Test
    fun `when getSurveys is executed and api return success should save data in local DB`() =
        runTest {
            //Given
            val metadata = SurveyMetaData(1, 1, 10, 100)
            val data = listOf(
                Survey(
                    "1",
                    "simple",
                    SurveyAttribute(
                        "title",
                        "description",
                        true,
                        "https://backgroundimage.png",
                        "simple"
                    )
                )
            )
            val responseSuccess = Response.success(
                SurveyResponse(data, metadata)
            )

            coEvery { surveyApi.getSurveys(any(), any()) } returns responseSuccess

            //When
            surveyRepository.getSurveys()

            //Then
            coVerify(exactly = 1) { surveyDao.deleteAllSurvey() }
            coVerify(exactly = 1) { surveyDao.insertSurvey(any()) }
        }

    @Test
    fun `when getSurveys is executed and api return an error try to get data from DB`() = runTest {
        //Given
        val responseError: Response<SurveyResponse> = Response.error(
            403,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery { surveyApi.getSurveys(any(), any()) } returns responseError

        //When
        surveyRepository.getSurveys()

        //Then
        coVerify(exactly = 0) { surveyDao.deleteAllSurvey() }
        coVerify(exactly = 0) { surveyDao.insertSurvey(any()) }
        coVerify(exactly = 1) { surveyDao.getSurveys() }
    }

}