package com.example.dreamcatcher_android.ui.story

import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStoryBinding
import com.example.dreamcatcher_android.util.navigateSafe

class StoryFragment : BaseFragment<FragmentStoryBinding>(R.layout.fragment_story) {

    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        initButtons()
    }

    private fun initButtons() {
        binding.fragmentStoryNextBt.setOnClickListener {
            val action = StoryFragmentDirections.actionStoryFragmentToStoryHintFragment()
            findNavController().navigateSafe(action.actionId)
        }
    }

}