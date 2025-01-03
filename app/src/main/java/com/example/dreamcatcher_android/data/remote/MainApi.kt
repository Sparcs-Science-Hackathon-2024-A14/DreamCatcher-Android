package com.example.dreamcatcher_android.data.remote

import com.example.dreamcatcher_android.domain.model.response.BadgeListResponse
import com.example.dreamcatcher_android.domain.model.response.LoginResponse
import com.example.dreamcatcher_android.domain.model.response.QuestPopupResponse
import com.example.dreamcatcher_android.domain.model.response.QuestResponse
import com.example.dreamcatcher_android.domain.model.response.SpotPositionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MainApi {
    // 로그인
    @POST("/api/login/{name}/{age}")
    suspend fun getLogin(
        @Path("name") name: String,
        @Path("age") age: Int
    ): Response<LoginResponse>

    // 뱃지 목록 조회
    @GET("/api/badge/{id}")
    suspend fun getBadgeList(
        @Path("id") id: Int
    ): Response<BadgeListResponse>

    // 해당 지역 내 스팟 좌표 반환 (1로 고정)
    @GET("/api/map/spot/{regionId}")
    suspend fun getSpotList(
        @Path("regionId") regionId: Int,
    ): Response<SpotPositionResponse>

    // 사용자 주변 스팟 추적 (실시간 좌표 전송)
    @GET("/api/map/nearby/{id}/{regionId}/{userX}/{userY}")
    suspend fun getSpotTracking(
        @Path("id") id: Int,
        @Path("regionId") regionId: Int,
        @Path("userX") userX: Double,
        @Path("userY") userY: Double,
    ): Response<QuestPopupResponse>

    // 퀘스트 목록
    @GET("/api/quest/{id}/{quest_id}/{next_process_id}")
    suspend fun getQuestProcess(
        @Path("id") id: Int,
        @Path("quest_id") quest_id: Int,
        @Path("next_process_id") next_process_id: Int
    ): Response<QuestResponse>

    // 마커 클릭
    @GET("/api/click/nearest-spot/{id}/{regionId}/{x}/{y}")
    suspend fun clickMarker(
        @Path("id") id: Int,
        @Path("regionId") regionId: Int,
        @Path("x") x: Double,
        @Path("y") y: Double
    ): Response<QuestPopupResponse>


}