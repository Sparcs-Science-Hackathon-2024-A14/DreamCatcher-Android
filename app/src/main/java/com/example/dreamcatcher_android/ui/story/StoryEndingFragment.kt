package com.example.dreamcatcher_android.ui.story

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStoryEndingBinding

class StoryEndingFragment : BaseFragment<FragmentStoryEndingBinding>(R.layout.fragment_story_ending) {

    override fun setLayout() {
        start()
    }

    fun start() {
        Log.d("fhrm", "dd")
    }

}