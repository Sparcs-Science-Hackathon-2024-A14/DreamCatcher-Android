package com.example.dreamcatcher_android.ui

import android.util.Log
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentBadgeBinding

class BadgeFragment : BaseFragment<FragmentBadgeBinding>(R.layout.fragment_badge) {

    override fun setLayout() {
        start()
    }

    fun start() {
        Log.d("fhrm", "dd")
    }

}