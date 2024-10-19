package com.example.dreamcatcher_android.domain.model.response

data class QuestResponse(
    val id: Long? = 0,
    val nextFirstId: Long? = 0,
    val nextSecondId: Long? = 0,
    val questType: String? = null,
    val isNextBranch: Boolean? = false,
    val isCurrentBranch: Boolean? = false,
    val processImg: String? = "",
    val processDescription: String? = "",
    val processTTS: String? = "",
    val optionFirst: String? = "",
    val optionSecond: String? = "",
    val firstHint: String? = "",
    val secondHint: String? = "",
    val nextBranch: Boolean? = false,
    val currentBranch: Boolean? = false
)