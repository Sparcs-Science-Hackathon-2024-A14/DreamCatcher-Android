package com.example.dreamcatcher_android.ui.siginin

import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentSignInBinding
import com.example.dreamcatcher_android.util.navigateSafe

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        initButtons()
    }

    private fun initButtons() {
        binding.fragmentSigninLoginIb.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
            findNavController().navigateSafe(action.actionId)
        }
    }

}