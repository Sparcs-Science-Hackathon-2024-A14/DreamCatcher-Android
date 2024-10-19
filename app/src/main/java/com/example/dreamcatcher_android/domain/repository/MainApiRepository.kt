package com.example.dreamcatcher_android.domain.repository

import com.example.dreamcatcher_android.domain.model.response.BadgeListResponse
import com.example.dreamcatcher_android.domain.model.response.LoginResponse
import com.example.dreamcatcher_android.domain.model.response.QuestPopupResponse
import com.example.dreamcatcher_android.domain.model.response.QuestResponse
import com.example.dreamcatcher_android.domain.model.response.SpotPositionResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MainApiRepository {
    suspend fun getLogin(name: String, age:Int) : Flow<Response<LoginResponse>>
    suspend fun getBadgeList(id: Int) : Flow<Response<BadgeListResponse>>
    suspend fun getSpotList(regionId: Int) : Flow<Response<SpotPositionResponse>>
    suspend fun getSpotTracking(regionId: Int, userX: Double, userY: Double) : Flow<Response<QuestPopupResponse>>
    suspend fun getQuestProcess(quest_id: Int, next_process_id: Int) : Flow<Response<QuestResponse>>
}