package com.example.dreamcatcher_android.domain.model.response

data class BadgeResponse(
    val badgeId: Long? = 0,
    val badgeName: String? = "",
    val badgeDescription: String? = "",
    val badgeImg: String? = ""
)

data class BadgeListResponse(
    val badgeDtoList: List<BadgeResponse>? = null
)