package com.eltex.androidschool.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import java.time.ZoneId

@InstallIn(ViewModelComponent::class)
@Module
class ZoneIdModule {
    @Provides
    fun provideZoneId(): ZoneId = ZoneId.systemDefault()
}