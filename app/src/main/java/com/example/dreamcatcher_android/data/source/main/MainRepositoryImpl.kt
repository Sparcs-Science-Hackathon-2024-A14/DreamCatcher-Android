package com.example.dreamcatcher_android.data.source.main

import com.example.dreamcatcher_android.data.remote.MainApi
import com.example.dreamcatcher_android.domain.repository.MainApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dataSource: MainApiDataSource
) : MainApiRepository {

//    override suspend fun postLogInInfo(logInDto: LogInDto): Flow<BaseResponse<String>>
//            = dataSource.postLogInInfo(logInDto)

}