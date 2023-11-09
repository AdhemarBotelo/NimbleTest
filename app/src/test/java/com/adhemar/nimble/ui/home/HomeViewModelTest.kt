package com.adhemar.nimble.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adhemar.nimble.data.SurveyRepository
import com.adhemar.nimble.data.local.database.SurveyDB
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @RelaxedMockK
    private lateinit var surveyRepository: SurveyRepository

    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        homeViewModel = HomeViewModel(surveyRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ui State should be initial when view model is created`() = runTest {
        assertEquals(homeViewModel.uiState.value, HomeUiState.Initial)
    }

    @Test
    fun `When error handler is called should clean error ui state`() = runTest {
        //Given

        //When
        homeViewModel.onErrorHandler()

        //Then
        assertEquals(homeViewModel.uiState.value, HomeUiState.Initial)
    }

    @Test
    fun `When load survey get success response ui state should be success whit the list of surveys`() =
        runTest {
            //Given
            val surveys = listOf(
                SurveyDB(
                    "title",
                    "id",
                    "description",
                    "image url"
                )
            )
            coEvery { surveyRepository.getSurveysFromDB() } returns surveys

            //When
            homeViewModel.loadSurveys()

            //Then
            assertEquals(HomeUiState.Success(surveys), homeViewModel.uiState.value)
        }

    @Test
    fun `When load survey get error response ui state should be error `() = runTest {
        //Given
        coEvery { surveyRepository.getSurveysFromDB() } returns emptyList()

        //When
        homeViewModel.loadSurveys()

        //Then
        assertTrue(homeViewModel.uiState.value is HomeUiState.Error)
    }

    @Test
    fun `When try load survey again get success response ui state should be success whit the list of surveys`() =
        runTest {
            //Given
            val surveys = listOf(
                SurveyDB(
                    "title",
                    "id",
                    "description",
                    "image url"
                )
            )
            coEvery { surveyRepository.getSurveys() } returns surveys

            //When
            homeViewModel.tryAgain()

            //Then
            assertEquals(HomeUiState.Success(surveys), homeViewModel.uiState.value)
        }

    @Test
    fun `When try load survey again get error response ui state should be error `() = runTest {
        //Given
        coEvery { surveyRepository.getSurveys() } returns emptyList()

        //When
        homeViewModel.tryAgain()

        //Then
        assertTrue(homeViewModel.uiState.value is HomeUiState.Error)
    }

}