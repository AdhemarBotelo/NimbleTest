package com.adhemar.nimble.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adhemar.nimble.data.SurveyRepository
import com.adhemar.nimble.data.local.database.SurveyDB
import com.adhemar.nimble.data.network.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val surveyRepository: SurveyRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Initial)
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadSurveys() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = HomeUiState.Error(error)
            }
        }) {
            surveyRepository.getSurveysFromDB()
                .collect { listSurveys ->
                    withContext(Dispatchers.Main) {
                        if (listSurveys.isNotEmpty()) {
                            _uiState.value = HomeUiState.Success(listSurveys)
                        } else {
                            _uiState.value = HomeUiState.Error(Exception("There is no Surveys"))
                        }
                    }
                }
        }
    }

    fun tryAgain() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = HomeUiState.Error(error)
            }
        }) {
            surveyRepository.getSurveys(viewModelScope)
                .collect { apiResponse ->
                    withContext(Dispatchers.Main) {
                        if (apiResponse is ApiResponse.Success) {
                            loadSurveys()
                        }
                    }
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