package com.example.dreamcatcher_android.data.source.main

import com.example.dreamcatcher_android.domain.model.response.BadgeListResponse
import com.example.dreamcatcher_android.domain.model.response.LoginResponse
import com.example.dreamcatcher_android.domain.model.response.QuestPopupResponse
import com.example.dreamcatcher_android.domain.model.response.QuestResponse
import com.example.dreamcatcher_android.domain.model.response.SpotPositionResponse
import com.example.dreamcatcher_android.domain.repository.MainApiRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dataSource: MainApiDataSource
) : MainApiRepository {

    override suspend fun getLogin(name: String, age: Int): Flow<Response<LoginResponse>> =
        dataSource.getLogin(name, age)

    override suspend fun getBadgeList(id: Int): Flow<Response<BadgeListResponse>> =
        dataSource.getBadgeList(id)

    override suspend fun getSpotList(regionId: Int): Flow<Response<SpotPositionResponse>> =
        dataSource.getSpotList(regionId)

    override suspend fun getSpotTracking(
        regionId: Int,
        userX: Double,
        userY: Double
    ): Flow<Response<QuestPopupResponse>> = dataSource.getSpotTracking(regionId, userX, userY)

    override suspend fun getQuestProcess(
        quest_id: Int,
        next_process_id: Int
    ): Flow<Response<QuestResponse>> = dataSource.getQuestProcess(quest_id, next_process_id)

}