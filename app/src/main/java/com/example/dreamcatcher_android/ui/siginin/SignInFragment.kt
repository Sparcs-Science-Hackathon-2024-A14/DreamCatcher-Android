package com.example.dreamcatcher_android.ui.siginin

import android.text.Editable
import android.text.TextWatcher
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
        activateSignInButton()
        initButtons()
    }

    private fun initButtons() {
        binding.fragmentSigninLoginBtn.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
            findNavController().navigateSafe(action.actionId)
        }
    }

    private fun activateSignInButton() {
        val disableBackground = R.drawable.ic_pupler5_btn
        val enableBackground = R.drawable.ic_gradation_btn

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val nameText = binding.fragmentSigninNameEt.text.toString().trim()
                val ageText = binding.fragmentSigninAgeEt.text.toString().trim()

                if(nameText.isNotEmpty() && ageText.isNotEmpty()) {
                    binding.fragmentSigninLoginBtn.setBackgroundResource(enableBackground)
                    binding.fragmentSigninLoginBtn.isEnabled = true
                } else {
                    binding.fragmentSigninLoginBtn.setBackgroundResource(disableBackground)
                    binding.fragmentSigninLoginBtn.isEnabled = true
                }
            }
        }
        binding.fragmentSigninNameEt.addTextChangedListener(textWatcher)
        binding.fragmentSigninAgeEt.addTextChangedListener(textWatcher)
    }

}