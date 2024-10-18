package com.example.dreamcatcher_android.util

import android.Manifest
import android.os.Build

object PermissionCheckList {
    val permissions = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> { // Android 13 (API 33) 이상
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> { // Android 12 (API 31) 부터 Android 12L (API 32)까지
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }
        Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> { // Android 10 (API 29) 부터 Android 11 (API 30)까지
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }
        else -> { // Android 9 (API 28) 이하
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }
    }

}