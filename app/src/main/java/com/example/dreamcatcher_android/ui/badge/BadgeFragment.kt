package com.example.dreamcatcher_android.ui.badge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentBadgeBinding
import com.example.dreamcatcher_android.domain.model.response.BadgeResponse
import com.example.dreamcatcher_android.ui.MainViewModel
import com.example.dreamcatcher_android.ui.siginin.SignInFragmentDirections
import com.example.dreamcatcher_android.util.GlobalApplication
import com.example.dreamcatcher_android.util.navigateSafe
import kotlinx.coroutines.launch

class BadgeFragment : BaseFragment<FragmentBadgeBinding>(R.layout.fragment_badge) {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        getBadgeList()
    }

    // 뱃지 목록에 따라 클릭 리스너 설정하는 함수
    private fun setupBadgeClickListeners(badgeList: List<BadgeResponse>) {

        val badgeImageViews = listOf(
            binding.fragmentBadgeBadge1Iv,
            binding.fragmentBadgeBadge2Iv,
            binding.fragmentBadgeBadge3Iv,
            binding.fragmentBadgeBadge4Iv,
            binding.fragmentBadgeBadge5Iv,
            binding.fragmentBadgeBadge6Iv
        )

        val badgeTextViews = listOf(
            binding.fragmentBadgeBadge1Tv,
            binding.fragmentBadgeBadge2Tv,
            binding.fragmentBadgeBadge3Tv,
            binding.fragmentBadgeBadge4Tv,
            binding.fragmentBadgeBadge5Tv,
            binding.fragmentBadgeBadge6Tv
        )

        // 각각의 ImageView에 클릭 리스너 설정
        badgeImageViews.forEachIndexed { index, imageView ->
            if (index < badgeList.size) {
                val badge = badgeList[index]

                Glide.with(this)
                    .load(badge.badgeImg)
                    .into(imageView)

                // 텍스트뷰에 뱃지 이름 설정
                badgeTextViews[index].text = badge.badgeName


                imageView.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("badgeName", badge.badgeName)
                        putString("badgeDescription", badge.badgeDescription)
                        putString("badgeImg", badge.badgeImg)
                    }

                    // Fragment 간 데이터 전달
                    val action = BadgeFragmentDirections.actionBadgeFragmentToBadgeDetailFragment()
                    findNavController().navigateSafe(action.actionId, bundle)
                }
            } else {
                // 이미지뷰가 뱃지 목록보다 많을 경우 처리
                imageView.setOnClickListener(null)
            }
        }
    }

    // 서버에서 뱃지 목록 가져오는 함수
    private fun getBadgeList() {
        val id = GlobalApplication.prefsManager.getUserId()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.getBadgeList(id)
                mainViewModel.badgeListResponse.collect { response ->
                    setupBadgeClickListeners(response.body()?.badgeDtoList ?: emptyList())
                }
            }
        }
    }

}