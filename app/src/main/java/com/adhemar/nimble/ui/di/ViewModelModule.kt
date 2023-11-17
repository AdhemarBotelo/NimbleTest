package com.adhemar.nimble.ui.di

import com.adhemar.nimble.data.ISurveyRepository
import com.adhemar.nimble.data.SecurityRepository
import com.adhemar.nimble.data.SurveyRepository
import com.adhemar.nimble.data.network.ITokenManager
import com.adhemar.nimble.data.network.TokenManager
import com.adhemar.nimble.ui.SplashViewModel
import com.adhemar.nimble.ui.home.HomeViewModel
import com.adhemar.nimble.ui.security.SecurityViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideSecurityViewModel(
        securityRepository: SecurityRepository,
        tokenManager: ITokenManager,
    ): SecurityViewModel {
        return SecurityViewModel(securityRepository, tokenManager)
    }

    @Provides
    fun provideHomeViewModel(
        surveyRepository: ISurveyRepository,
        tokenManager: ITokenManager,
    ): HomeViewModel {
        return HomeViewModel(surveyRepository, tokenManager)
    }

    @Provides
    fun provideSplashViewModel(
        surveyRepository: ISurveyRepository,
        tokenManager: ITokenManager,
    ): SplashViewModel {
        return SplashViewModel(surveyRepository, tokenManager)
    }
}