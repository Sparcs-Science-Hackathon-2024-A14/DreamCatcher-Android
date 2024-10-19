package com.example.dreamcatcher_android.ui.badge


import com.bumptech.glide.Glide
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentBadgeDetailBinding

class BadgeDetailFragment : BaseFragment<FragmentBadgeDetailBinding>(R.layout.fragment_badge_detail) {

    override fun setLayout() {
        setupBadgeUI()
    }

    private fun setupBadgeUI() {
        // arguments에서 데이터 받아오기
        arguments?.let { args ->
            val badgeName = args.getString("badgeName")
            val badgeDescription = args.getString("badgeDescription")
            val badgeImg = args.getString("badgeImg")

            // 받아온 데이터를 뷰에 바인딩
            binding.apply {
                // 이미지 URL을 이미지뷰에 설정 (Glide 사용)
                Glide.with(this@BadgeDetailFragment)
                    .load(badgeImg)
                    .into(fragmentBadgeDetailBadgeIv)

                // 텍스트 뷰에 이름과 설명 설정
                fragmentBadgeDetailNameTv.text = badgeName
                fragmentBadgeDetailStory1Tv.text = badgeDescription
            }
        } ?: run {
            // arguments가 null인 경우 처리 (예: 기본 데이터 설정)
            binding.apply {
                fragmentBadgeDetailNameTv.text = getString(R.string.badge_example_title)
                fragmentBadgeDetailStory1Tv.text = getString(R.string.science_story_example1)
            }
        }
    }
}