package com.example.dreamcatcher_android.ui.start

import android.util.Log
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStartBinding

class StartFragment : BaseFragment<FragmentStartBinding>(R.layout.fragment_start) {

    override fun setLayout() {
        start()
    }

    fun start() {
        Log.d("로그","ㅇ")
    }

}