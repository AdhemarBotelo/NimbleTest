package com.adhemar.nimble.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adhemar.nimble.data.ISurveyRepository
import com.adhemar.nimble.data.local.database.SurveyDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val surveyRepository: ISurveyRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Initial)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadSurveys() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = HomeUiState.Error(error)
            }
        }) {
            val surveys = surveyRepository.getSurveysFromDB()
            if (surveys.isEmpty()) {
                _uiState.value = HomeUiState.Error(Exception("There is no Surveys"))

            } else {
                _uiState.value = HomeUiState.Success(surveys)
            }

        }
    }

    fun tryAgain() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = HomeUiState.Error(error)
            }
        }) {
            val surveys = surveyRepository.getSurveys()
            if (surveys.isEmpty()) {
                _uiState.value = HomeUiState.Error(Exception("There is no Surveys"))

            } else {
                _uiState.value = HomeUiState.Success(surveys)
            }
        }
    }

    fun onErrorHandler() {
        _uiState.value = HomeUiState.Initial
    }
}

sealed interface HomeUiState {
    data object Initial : HomeUiState
    data object Loading : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
    data class Success(val surveys: List<SurveyDB>) : HomeUiState
}