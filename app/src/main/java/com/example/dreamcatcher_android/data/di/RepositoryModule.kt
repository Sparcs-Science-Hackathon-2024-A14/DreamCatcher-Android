package com.example.dreamcatcher_android.data.di

import com.example.dreamcatcher_android.data.source.main.MainApiDataSource
import com.example.dreamcatcher_android.data.source.main.MainRepositoryImpl
import com.example.dreamcatcher_android.domain.repository.MainApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainApiRepository(mainApiDataSource: MainApiDataSource) : MainApiRepository =
        MainRepositoryImpl(mainApiDataSource)

}