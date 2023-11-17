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


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adhemar.nimble.data.SecurityRepository
import com.adhemar.nimble.data.network.ApiResponse
import com.adhemar.nimble.data.network.TokenManager
import com.adhemar.nimble.data.network.model.LogInResponse
import com.adhemar.nimble.data.network.model.LoginRequest
import com.adhemar.nimble.data.network.model.OauthToken
import com.adhemar.nimble.data.network.model.OauthTokenAtribute
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SecurityViewModelTest {
    @RelaxedMockK
    private lateinit var securityRepository: SecurityRepository

    @RelaxedMockK
    private lateinit var tokenManager: TokenManager

    private lateinit var securityViewModel: SecurityViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        securityViewModel = SecurityViewModel(securityRepository, tokenManager = tokenManager)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ui State should be initial when view model is created`() = runTest {
        assertEquals(securityViewModel.uiState.value, SecurityUiState.Initial)
    }

    @Test
    fun `When error handler is called should clean error ui state`() = runTest {
        //Given

        //When
        securityViewModel.onErrorHandler()

        //Then
        assertEquals(securityViewModel.uiState.value, SecurityUiState.Initial)
    }

    @Test
    fun `When user perform a login and it is success should save token and update ui state`() =
        runTest {
            //Given
            val email = "email"
            val pwd = "pwd"
            val loginSuccess = LogInResponse(
                OauthToken(
                    "1",
                    "password",
                    OauthTokenAtribute(
                        "accesstoken123",
                        "token",
                        4000,
                        "refreshToken324",
                        60000
                    )
                )
            )
            coEvery {
                securityRepository.login(
                    LoginRequest(
                        email = email,
                        password = pwd
                    )
                )
            } returns
                    flow {
                        emit(ApiResponse.Success(loginSuccess))
                    }

            //When
            securityViewModel.performLogin(email, pwd)


            //Then
            coVerify(exactly = 1) { tokenManager.saveToken(any()) }
            coVerify(exactly = 1) { tokenManager.saveRefreshToken(any()) }
            assertEquals(securityViewModel.uiState.value, SecurityUiState.Success)
        }

    @Test
    fun `When user perform a login and it is not success should update ui state`() = runTest {
        //Given
        val email = "email"
        val pwd = "pwd"
        val response = ApiResponse.Failure("error", 400)
        coEvery {
            securityRepository.login(
                LoginRequest(
                    email = email,
                    password = pwd,
                )
            )
        } returns
                flow {
                    emit(response)
                }

        //When
        securityViewModel.performLogin(email, pwd)


        //Then
        assertTrue(securityViewModel.uiState.value is SecurityUiState.Error)
    }
}
