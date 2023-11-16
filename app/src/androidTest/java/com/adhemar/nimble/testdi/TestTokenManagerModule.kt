package com.adhemar.nimble.testdi

import com.adhemar.nimble.data.di.FakeTokenManager
import com.adhemar.nimble.data.di.TokenManagerModule
import com.adhemar.nimble.data.network.ApiService
import com.adhemar.nimble.data.network.ITokenManager
import com.adhemar.nimble.data.network.model.Survey
import com.adhemar.nimble.data.network.model.SurveyAttribute
import com.adhemar.nimble.data.network.model.SurveyMetaData
import com.adhemar.nimble.data.network.model.SurveyResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TokenManagerModule::class]
)
abstract class FakeTokenManagerModule {
    @Singleton
    @Binds
    abstract fun bindTokenManager(
        fakeRepository: FakeTokenManager
    ): ITokenManager
}

class FakeApiService @Inject constructor() : ApiService {
    override suspend fun getSurveys(pageNumber: Int, pageSize: Int): Response<SurveyResponse> {

        return Response.success(
            SurveyResponse(
                fakeSurveys,
                SurveyMetaData(1, 5, 100, 100)
            )
        )
    }
}

val fakeSurveys = listOf(
    Survey("id", "type", SurveyAttribute("title", "description", true, "URL", "type")),
    Survey("id2", "type", SurveyAttribute("title2", "description2", true, "URL2", "type"))
)