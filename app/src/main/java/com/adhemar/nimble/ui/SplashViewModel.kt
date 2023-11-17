package com.adhemar.nimble.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adhemar.nimble.data.ISurveyRepository
import com.adhemar.nimble.data.network.ITokenManager
import com.adhemar.nimble.ui.security.SecurityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val surveyRepository: ISurveyRepository,
    private val tokenManager: ITokenManager,

    ) : ViewModel() {

    private val _uiState: MutableStateFlow<SplashUiState> =
        MutableStateFlow(SplashUiState.Initial)
    val uiState: StateFlow<SplashUiState> = _uiState
    fun getSurveys() {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = SplashUiState.Initial
            }
        })
        {
            surveyRepository.getSurveys()
        }
    }

    fun isLogged() {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = SplashUiState.NotLogged
            }
        }) {
            tokenManager.getLoginStatus().collect() {
                withContext(Dispatchers.Main) {
                    _uiState.value = if (it) SplashUiState.IsLogged else SplashUiState.NotLogged
                }
            }
        }
    }
}

sealed interface SplashUiState {
    data object IsLogged : SplashUiState
    data object Initial : SplashUiState
    data object NotLogged : SplashUiState
}