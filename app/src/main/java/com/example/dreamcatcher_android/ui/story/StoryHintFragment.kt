package com.example.dreamcatcher_android.ui.story

import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStoryHintBinding
import com.example.dreamcatcher_android.util.dialog.HintDialogFragment
import com.example.dreamcatcher_android.util.navigateSafe

class StoryHintFragment : BaseFragment<FragmentStoryHintBinding>(R.layout.fragment_story_hint) {

    override fun setLayout() {
        initButton()
    }

    private fun initButton() {
        // 임시 버튼 클릭
        binding.fragmentStoryHintBt1.setOnClickListener {
            val action = StoryHintFragmentDirections.actionStoryHintFragmentToStoryEndingFragment()
            findNavController().navigateSafe(action.actionId)
        }

        // 힌트 버튼 클릭
        binding.fragmentStoryHintShowHintBt.setOnClickListener {
            HintDialogFragment("힌트힌트힌트").show(parentFragmentManager, "ShowHintDialog")
        }
    }

}