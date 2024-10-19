package com.example.dreamcatcher_android.util

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {

    companion object{
        lateinit var prefsManager : PreferenceUtil
    }

    override fun onCreate() {
        prefsManager = PreferenceUtil(applicationContext)
        super.onCreate()
    }

}