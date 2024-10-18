package com.example.dreamcatcher_android.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    // 현재 목적지와 이동할 목적지 비교 (목적지 중복 이동 방지)
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null && currentDestination?.id != action.destinationId) {
        val newNavOptions = navOptions?.let { NavOptions.Builder() } ?: NavOptions.Builder()
        newNavOptions.setPopUpTo(action.destinationId, true)    // 현재 프래그먼트 ~ 목적지 프래그먼트 사이의 백스택 제거
        navigate(resId, args, newNavOptions.build(), navExtras)
    }
}