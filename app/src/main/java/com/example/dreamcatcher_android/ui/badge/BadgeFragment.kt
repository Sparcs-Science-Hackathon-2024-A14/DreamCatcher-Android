package com.example.dreamcatcher_android.ui.badge

import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentBadgeBinding
import com.example.dreamcatcher_android.ui.siginin.SignInFragmentDirections
import com.example.dreamcatcher_android.util.navigateSafe

class BadgeFragment : BaseFragment<FragmentBadgeBinding>(R.layout.fragment_badge) {

    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        initButtons()
    }

    private fun initButtons() {
        binding.fragmentBadgeBadge1Iv.setOnClickListener {
            val action = BadgeFragmentDirections.actionBadgeFragmentToBadgeDetailFragment()
            findNavController().navigateSafe(action.actionId)
        }

        // 임시버튼
        binding.fragmentBadgeBadge2Iv.setOnClickListener {
            val action = BadgeFragmentDirections.actionBadgeFragmentToStoryFragment()
            findNavController().navigateSafe(action.actionId)
        }
    }

}