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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.adhemar.nimble.data.local.database.Security
import com.adhemar.nimble.data.local.database.SurveyDao

/**
 * Unit tests for [DefaultSecurityRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultSecurityRepositoryTest {

    @Test
    fun securitys_newItemSaved_itemIsReturned() = runTest {
//        val repository = DefaultSecurityRepository(FakeSecurityDao())
//
//        repository.add("Repository")
//
//        assertEquals(repository.securitys.first().size, 1)
    }

}

private class FakeSecurityDao : SurveyDao {

    private val data = mutableListOf<Security>()

    override fun getSecuritys(): Flow<List<Security>> = flow {
        emit(data)
    }

    override suspend fun insertSecurity(item: Security) {
        data.add(0, item)
    }
}
