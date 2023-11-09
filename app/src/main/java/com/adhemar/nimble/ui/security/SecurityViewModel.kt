/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adhemar.nimble.ui.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adhemar.nimble.data.SecurityRepository
import com.adhemar.nimble.data.network.ApiResponse
import com.adhemar.nimble.data.network.TokenManager
import com.adhemar.nimble.data.network.model.LoginRequest
import com.adhemar.nimble.ui.security.SecurityUiState.Error
import com.adhemar.nimble.ui.security.SecurityUiState.Loading
import com.adhemar.nimble.ui.security.SecurityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val securityRepository: SecurityRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SecurityUiState> =
        MutableStateFlow(SecurityUiState.Initial)
    val uiState: StateFlow<SecurityUiState> = _uiState


    fun performLogin(email: String, password: String) {
        _uiState.value = Loading
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                _uiState.value = Error(Exception(error))
            }
        }) {
            securityRepository.login(LoginRequest(email = email, password = password))
                .collect { apiResponse ->
                    withContext(Dispatchers.Main) {
                        if (apiResponse is ApiResponse.Success) {
                            tokenManager.deleteToken()
                            tokenManager.deleteRefreshToken()
                            tokenManager.saveToken(apiResponse.data.data.attributes.accessToken)
                            tokenManager.saveRefreshToken(apiResponse.data.data.attributes.refreshToken)
                            _uiState.value = Success
                        } else if (apiResponse is ApiResponse.Failure) {
                            _uiState.value = Error(Exception(apiResponse.errorMessage))
                        }
                    }
                }
        }
    }

    fun onErrorHandler() {
        _uiState.value = SecurityUiState.Initial
    }
}

sealed interface SecurityUiState {
    data object Initial : SecurityUiState
    data object Loading : SecurityUiState
    data class Error(val throwable: Exception) : SecurityUiState
    data object Success : SecurityUiState
}
