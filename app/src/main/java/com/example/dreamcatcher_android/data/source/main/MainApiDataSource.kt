package com.example.dreamcatcher_android.data.source.main

import android.util.Log
import com.example.dreamcatcher_android.data.remote.MainApi
import com.example.dreamcatcher_android.domain.model.response.BadgeListResponse
import com.example.dreamcatcher_android.domain.model.response.LoginResponse
import com.example.dreamcatcher_android.domain.model.response.QuestPopupResponse
import com.example.dreamcatcher_android.domain.model.response.QuestResponse
import com.example.dreamcatcher_android.domain.model.response.SpotPositionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class MainApiDataSource @Inject constructor(
    private val mainApi: MainApi
) {

     fun getLogin(name: String, age:Int) : Flow<Response<LoginResponse>> = flow {
          try {
               val result = mainApi.getLogin(name, age)
               emit(result)
          } catch (e: HttpException) {
               val errorResponse = e.response()
               Log.e("getLogin Failure", "HTTP Error: ${errorResponse?.errorBody()?.string()}")
          }
     }

     fun getBadgeList(id: Int) : Flow<Response<BadgeListResponse>> = flow {
          try {
               val result = mainApi.getBadgeList(id)
               emit(result)
          } catch (e: HttpException) {
               val errorResponse = e.response()
               Log.e("getBadgeList Failure", "HTTP Error: ${errorResponse?.errorBody()?.string()}")
          }
     }

     fun getSpotList(regionId: Int) : Flow<Response<SpotPositionResponse>> = flow {
          try {
               val result = mainApi.getSpotList(regionId)
               emit(result)
          } catch (e: HttpException) {
               val errorResponse = e.response()
               Log.e("getSpotList Failure", "HTTP Error: ${errorResponse?.errorBody()?.string()}")
          }
     }

     fun getSpotTracking(regionId: Int, userX: Double, userY: Double) : Flow<Response<QuestPopupResponse>> = flow {
          try {
               val result = mainApi.getSpotTracking(regionId, userX, userY)
               emit(result)
          } catch (e: HttpException) {
               val errorResponse = e.response()
               Log.e("getSpotTracking Failure", "HTTP Error: ${errorResponse?.errorBody()?.string()}")
          }
     }


     fun clickMarker(regionId: Int, x: Double, y: Double) : Flow<Response<QuestPopupResponse>> = flow {
          try {
               val result = mainApi.clickMarker(regionId, x, y)
               emit(result)
          } catch (e: HttpException) {
               val errorResponse = e.response()
               Log.e("getSpotTracking Failure", "HTTP Error: ${errorResponse?.errorBody()?.string()}")
          }
     }


     fun getQuestProcess(quest_id: Int, next_process_id: Int) : Flow<Response<QuestResponse>> = flow {
          try {
               val result = mainApi.getQuestProcess(quest_id, next_process_id)
               emit(result)
          } catch (e: HttpException) {
               val errorResponse = e.response()
               Log.e("getQuestProcess Failure", "HTTP Error: ${errorResponse?.errorBody()?.string()}")
          }
     }

}