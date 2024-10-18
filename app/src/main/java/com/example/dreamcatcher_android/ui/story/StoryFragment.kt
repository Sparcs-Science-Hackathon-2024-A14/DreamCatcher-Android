package com.example.dreamcatcher_android.ui.story

import android.util.Log
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStoryBinding

class StoryFragment : BaseFragment<FragmentStoryBinding>(R.layout.fragment_story) {

    override fun setLayout() {
        start()
    }

    fun start() {
        Log.d("fhrm", "dd")
    }

}