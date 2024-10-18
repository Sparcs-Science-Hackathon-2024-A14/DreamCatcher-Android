package com.example.dreamcatcher_android.ui.start

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStartBinding
import com.example.dreamcatcher_android.util.navigateSafe

class StartFragment : BaseFragment<FragmentStartBinding>(R.layout.fragment_start) {

    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        initButtons()
    }

    private fun initButtons() {
        binding.fragmentStartStartIb.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToSignInFragment()
            findNavController().navigateSafe(action.actionId)
        }
    }

}