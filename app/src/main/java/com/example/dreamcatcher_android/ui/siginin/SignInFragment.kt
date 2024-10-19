package com.example.dreamcatcher_android.ui.siginin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentSignInBinding
import com.example.dreamcatcher_android.ui.MainViewModel
import com.example.dreamcatcher_android.util.GlobalApplication
import com.example.dreamcatcher_android.util.PreferenceUtil
import com.example.dreamcatcher_android.util.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        activateSignInButton()
        initButtons()
    }

    private fun initButtons() {
        binding.fragmentSigninLoginBtn.setOnClickListener {
            val name = binding.fragmentSigninNameEt.text.toString()
            val age = binding.fragmentSigninAgeEt.text.toString().toInt()
            getSignIn(name, age)
//            val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
//            findNavController().navigateSafe(action.actionId)
        }
    }

    private fun getSignIn(name: String, age: Int) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.getLogin(name, age)
                mainViewModel.loginResponse.collect { res ->
                    if (res.isSuccessful && res.code() == 200) {
                        val userId = res.body()?.id?.toInt()
                        if (userId != null){
                            res.body()?.id?.let { GlobalApplication.prefsManager.saveUserId(userId) } // id 저장 -> 뱃지 조회
                            if(GlobalApplication.prefsManager.getUserId() != 0) {
                                val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
                                findNavController().navigateSafe(action.actionId)
                            }
                        }
                    }
                }
            }
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