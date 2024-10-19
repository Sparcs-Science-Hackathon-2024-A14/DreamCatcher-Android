package com.example.dreamcatcher_android.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USER_ID = "user_id"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // 사용자 ID 저장
    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt(KEY_USER_ID, userId).apply()
    }

    // 저장된 사용자 ID 조회
    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 1)
    }

    // 사용자 ID 삭제
    fun clearUserId() {
        sharedPreferences.edit().remove(KEY_USER_ID).apply()
    }
}
