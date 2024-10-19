package com.example.dreamcatcher_android.domain.model.response

data class QuestPopupResponse (
    val questId: Long? = 0,
    val questName: String? = "",
    val questImg: String? = "",
    val questDescription: String? = ""
)

data class SpotPositionResponse(
    val id: Long? = 0,
    val posX: Double? = 0.0,
    val posY: Double?= 0.0
)