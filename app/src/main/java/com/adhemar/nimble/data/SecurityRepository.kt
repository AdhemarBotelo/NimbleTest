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

package com.adhemar.nimble.data

import kotlinx.coroutines.flow.Flow
import com.adhemar.nimble.data.network.ApiResponse
import com.adhemar.nimble.data.network.AuthApiService
import com.adhemar.nimble.data.network.apiRequestFlow
import com.adhemar.nimble.data.network.model.LogInResponse
import com.adhemar.nimble.data.network.model.LoginRequest
import com.adhemar.nimble.data.network.model.RefreshTokenRequest
import javax.inject.Inject

interface SecurityRepository {

    fun login(loginRequest: LoginRequest):Flow<ApiResponse<LogInResponse>>

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest):Flow<ApiResponse<LogInResponse>>
}

class DefaultSecurityRepository @Inject constructor(
    private val authApiService: AuthApiService
) : SecurityRepository {
    override fun login(loginRequest: LoginRequest): Flow<ApiResponse<LogInResponse>> {
        return apiRequestFlow {
            authApiService.logIn(loginRequest)
        }
    }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Flow<ApiResponse<LogInResponse>> {
        return apiRequestFlow {
            authApiService.refreshToken(refreshTokenRequest)
        }
    }
}
