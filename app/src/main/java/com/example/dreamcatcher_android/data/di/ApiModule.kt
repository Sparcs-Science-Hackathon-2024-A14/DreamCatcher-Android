package com.example.dreamcatcher_android.data.di

import com.example.dreamcatcher_android.data.remote.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun mainApi(
        @NetworkModule.SpringRetrofit retrofit: Retrofit
    ) : MainApi = retrofit.create(MainApi::class.java)

}