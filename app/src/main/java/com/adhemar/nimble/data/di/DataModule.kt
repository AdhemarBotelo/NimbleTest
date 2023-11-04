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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.adhemar.nimble.data.SecurityRepository
import com.adhemar.nimble.data.DefaultSecurityRepository
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
}

class FakeSecurityRepository @Inject constructor() : SecurityRepository {
    override val securitys: Flow<List<String>> = flowOf(fakeSecuritys)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }
}

val fakeSecuritys = listOf("One", "Two", "Three")
