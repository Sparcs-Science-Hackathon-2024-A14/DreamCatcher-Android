package com.example.dreamcatcher_android.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dreamcatcher_android.domain.model.response.BadgeListResponse
import com.example.dreamcatcher_android.domain.model.response.LoginResponse
import com.example.dreamcatcher_android.domain.model.response.QuestPopupResponse
import com.example.dreamcatcher_android.domain.model.response.QuestResponse
import com.example.dreamcatcher_android.domain.model.response.SpotPositionResponse
import com.example.dreamcatcher_android.domain.repository.MainApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainApiRepository: MainApiRepository
) : ViewModel() {

    private val _loginResponse = MutableStateFlow(Response.success(LoginResponse()))
    val loginResponse: StateFlow<Response<LoginResponse>> = _loginResponse

    private val _badgeListResponse = MutableStateFlow(Response.success(BadgeListResponse()))
    val badgeListResponse: StateFlow<Response<BadgeListResponse>> = _badgeListResponse

    private val _spotPositionResponse = MutableStateFlow(Response.success(SpotPositionResponse()))
    val spotPositionResponse: StateFlow<Response<SpotPositionResponse>> = _spotPositionResponse

    private val _questPopupResponse = MutableStateFlow(Response.success(QuestPopupResponse()))
    val questPopupResponse: StateFlow<Response<QuestPopupResponse>> = _questPopupResponse

    private val _clickQuestPopupResponse = MutableStateFlow(Response.success(QuestPopupResponse()))
    val clickQuestPopupResponse: StateFlow<Response<QuestPopupResponse>> = _clickQuestPopupResponse

    private val _questResponse = MutableStateFlow(Response.success(QuestResponse()))
    val questResponse: StateFlow<Response<QuestResponse>> = _questResponse

    // 로그인
    fun getLogin(name: String, age: Int) {
        viewModelScope.launch {
            try {
                mainApiRepository.getLogin(name, age).collect {
                    _loginResponse.value = it
                }
            } catch (e:Exception) {
                Log.e("ViewModel getLogin Error", e.message.toString())
            }
        }
    }

    // 뱃지 목록
    fun getBadgeList(id: Int) {
        viewModelScope.launch {
            try {
                mainApiRepository.getBadgeList(id).collect {
                    _badgeListResponse.value = it
                }
            } catch (e:Exception) {
                Log.e("ViewModel getBadgeList Error", e.message.toString())
            }
        }
    }

    fun getSpotList(regionId: Int) {
        viewModelScope.launch {
            try {
                mainApiRepository.getSpotList(regionId).collect {
                    _spotPositionResponse.value = it
                }
            } catch (e:Exception) {
                Log.e("ViewModel getSpotList Error", e.message.toString())
            }
        }
    }

    // 사용자 주변 스팟 추적
    fun getSpotTracking(regionId: Int, userX: Double, userY: Double) {
        viewModelScope.launch {
            try {
                mainApiRepository.getSpotTracking(regionId, userX, userY).collect {
                    _questPopupResponse.value = it
                    Log.d("ㄹㄹㄹㄹㄹ", "${_questPopupResponse.value.body()}")
                }
            } catch (e:Exception) {
                Log.e("ViewModel getSpotList Error", e.message.toString())
            }
        }
    }

    // 마커 클릭
    // 사용자 주변 스팟 추적
    fun clickMarker(regionId: Int, userX: Double, userY: Double) {
        viewModelScope.launch {
            try {
                mainApiRepository.clickMarker(regionId, userX, userY).collect {
                    _questPopupResponse.value = it
                    Log.d("ㄹㄹㄹㄹㄹ", "${_questPopupResponse.value.body()}")
                }
            } catch (e:Exception) {
                Log.e("ViewModel clickMarker Error", e.message.toString())
            }
        }
    }

    // 퀘스트 호출
    fun getQuestProcess(quest_id: Int, next_process_id: Int) {
        viewModelScope.launch {
            try {
                mainApiRepository.getQuestProcess(quest_id, next_process_id).collect {
                    _questResponse.value = it
                }
            } catch (e:Exception) {
                Log.e("ViewModel getQuestProcess Error", e.message.toString())
            }
        }
    }

}