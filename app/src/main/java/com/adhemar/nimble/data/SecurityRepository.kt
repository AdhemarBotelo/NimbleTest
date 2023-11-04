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
import kotlinx.coroutines.flow.map
import com.adhemar.nimble.data.local.database.Security
import com.adhemar.nimble.data.local.database.SecurityDao
import javax.inject.Inject

interface SecurityRepository {
    val securitys: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultSecurityRepository @Inject constructor(
    private val securityDao: SecurityDao
) : SecurityRepository {

    override val securitys: Flow<List<String>> =
        securityDao.getSecuritys().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        securityDao.insertSecurity(Security(name = name))
    }
}
