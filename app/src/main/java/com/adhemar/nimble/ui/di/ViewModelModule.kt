package com.adhemar.nimble.ui.di

import com.adhemar.nimble.data.SecurityRepository
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
        securityRepository: SecurityRepository
    ): SecurityViewModel{
        return SecurityViewModel(securityRepository)
    }
}