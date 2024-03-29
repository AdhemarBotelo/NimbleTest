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

package com.adhemar.nimble.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class SurveyDB(
    @ColumnInfo(name = "title") val title: String,
    @PrimaryKey val id: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "background_image_url") val backgroundImageUrl: String
) {

}

@Dao
interface SurveyDao {
    @Query("SELECT * FROM SurveyDB ORDER BY id")
    suspend fun getSurveys(): List<SurveyDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(surveys: List<SurveyDB>)

    @Query("DELETE FROM SurveyDB")
    suspend fun deleteAllSurvey()
}
