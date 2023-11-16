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

package com.adhemar.nimble.testdi

import com.adhemar.nimble.data.ISurveyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import com.adhemar.nimble.data.SecurityRepository
import com.adhemar.nimble.data.SurveyRepository
import com.adhemar.nimble.data.di.DataModule
import com.adhemar.nimble.data.di.FakeSecurityRepository
import com.adhemar.nimble.data.di.FakeSurveyRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface FakeDataModule {

    @Binds
    abstract fun bindsSecurityRepository(
        fakeRepository: FakeSecurityRepository
    ): SecurityRepository


    @Binds
    abstract fun bindSurveyRepository(
        fakeRepository: FakeSurveyRepository
    ): ISurveyRepository
}
