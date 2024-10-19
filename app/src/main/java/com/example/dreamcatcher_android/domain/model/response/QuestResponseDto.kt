package com.example.dreamcatcher_android.domain.model.response

import com.example.dreamcatcher_android.util.enums.QuestType

data class QuestResponse(
    val id: Long? = 0,
    val nextFirstId: Long? = 0,
    val nextSecondId: Long? = 0,
    val questType: QuestType? = null,
    val processImg: String? = "",
    val processDescription: String? = "",
    val processTTS: String? = "",
    val optionFirst: String? = "",
    val optionSecond: String? = ""
)