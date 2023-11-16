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

package com.adhemar.nimble.data.di

import com.adhemar.nimble.data.DefaultSecurityRepository
import com.adhemar.nimble.data.ISurveyRepository
import com.adhemar.nimble.data.SecurityRepository
import com.adhemar.nimble.data.SurveyRepository
import com.adhemar.nimble.data.local.database.SurveyDB
import com.adhemar.nimble.data.network.ApiResponse
import com.adhemar.nimble.data.network.model.LogInResponse
import com.adhemar.nimble.data.network.model.LoginRequest
import com.adhemar.nimble.data.network.model.OauthToken
import com.adhemar.nimble.data.network.model.OauthTokenAtribute
import com.adhemar.nimble.data.network.model.RefreshTokenRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsSecurityRepository(
        securityRepository: DefaultSecurityRepository
    ): SecurityRepository

    @Singleton
    @Binds
    fun bindSurveyRepository(
        surveyRepository: SurveyRepository
    ): ISurveyRepository
}

class FakeSecurityRepository @Inject constructor() : SecurityRepository {
    override fun login(loginRequest: LoginRequest): Flow<ApiResponse<LogInResponse>> {
        return flow {
            emit(
                ApiResponse.Success(
                    LogInResponse(
                        data = OauthToken(
                            id = "id",
                            type = "type",
                            attributes = OauthTokenAtribute(
                                accessToken = "1234135dsafasdf",
                                tokenType = "type",
                                expiresIn = 8000,
                                refreshToken = "asdfadf1234asdf",
                                createdAt = 10000,
                            )

                        )
                    )
                )
            )
        }
    }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): Flow<ApiResponse<LogInResponse>> {
        return flow {
            emit(
                ApiResponse.Success(
                    LogInResponse(
                        data = OauthToken(
                            id = "id",
                            type = "type",
                            attributes = OauthTokenAtribute(
                                accessToken = "1234135dsafasdf",
                                tokenType = "type",
                                expiresIn = 8000,
                                refreshToken = "asdfadf1234asdf",
                                createdAt = 10000,
                            )

                        )
                    )
                )
            )
        }
    }
}

class FakeSurveyRepository @Inject constructor() : ISurveyRepository {
    override suspend fun getSurveys(): List<SurveyDB> {
        return fakeSurveys

    }

    override suspend fun getSurveysFromDB(): List<SurveyDB> {
        return fakeSurveys
    }
}

val fakeSurveys = listOf(
    SurveyDB("title", "id", "description", "url"),
    SurveyDB("title2", "id2", "description", "url")
)
