package com.example.dreamcatcher_android.ui.story

import android.media.MediaPlayer
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStoryBinding
import com.example.dreamcatcher_android.ui.MainViewModel
import com.example.dreamcatcher_android.util.GlobalApplication
import com.example.dreamcatcher_android.util.navigateSafe
import kotlinx.coroutines.launch

class StoryFragment : BaseFragment<FragmentStoryBinding>(R.layout.fragment_story) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var startQuestId = 0 // 첫 퀘스트 아이디
    //private var exitQuestProcessId = 0 // 엔딩 화면 번호
    private var nextQuestId = 0 // 다음 퀘스트 아이디
    private var currentBranch = false // 다음 분기가 Branch 인지 T/F

    private var option1 = 0 // 1번 선택지 ID
    private var option2 = 0 // 2번 선택지 ID
    private var mediaPlayer: MediaPlayer? = null // MediaPlayer 객체 추가

    override fun setLayout() {
        initSettings()
    }

    private fun getQuestId() {
        arguments?.let {
            startQuestId = it.getInt("questId")
        }
    }

    private fun initSettings() {
        getQuestId()
        getFirstStory()
        initButtons()
   }

    // 첫 퀘스트 호출
    private fun getFirstStory() {
        getQuestProcess(1)
        getUpdateUI()
    }

    private fun initButtons() {

        // 선택지 1 버튼 클릭 (선택지1, 다음으로)
        binding.fragmentStoryNextBt1.setOnClickListener {
            if (option1 != 0) {
                getQuestProcess(option1)
                getUpdateUI()
            }
        }

        // 선택지 2 버튼 클릭
        binding.fragmentStoryNextBt2.setOnClickListener {
            if (option2 != 0) {
                getQuestProcess(option2)
                getUpdateUI()
            }
        }
    }

    // 선택지 분기 여부에 따른 버튼 설정 및 텍스트 업데이트
    private fun updateButtons(isBranch: Boolean, optionFirst: String?, optionSecond: String?) {
        if (isBranch) {
            // 선택지가 두 개인 경우
            binding.fragmentStoryNextBt1.text = optionFirst // 첫 번째 선택지
            binding.fragmentStoryNextBt2.text = optionSecond // 두 번째 선택지
            binding.fragmentStoryNextBt1.visibility = View.VISIBLE
            binding.fragmentStoryNextBt2.visibility = View.VISIBLE
        } else {
            // 선택지가 하나인 경우
            binding.fragmentStoryNextBt1.text = "다음으로" // 텍스트를 "다음으로"로 설정
            binding.fragmentStoryNextBt1.visibility = View.VISIBLE
            binding.fragmentStoryNextBt2.visibility = View.GONE
        }
    }

    // isNextBranch -> 다음 것이 Branch 인지 반환
    private fun getQuestProcess(nextQuestId: Int) {
        mainViewModel.getQuestProcess(GlobalApplication.prefsManager.getUserId(), startQuestId, nextQuestId)
    }

    // 서버에서 정보를 받아 UI 업데이트
    private fun getUpdateUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.questResponse.collect { res ->
                    val body = res.body()
                    binding.fragmentStoryContentTv.text = body?.processDescription // 설명
                    setImageView(body?.processImg.toString()) // 이미지

                    if(body?.nextFirstId?.toInt() == null) {

                    }

                    option1 = body?.nextFirstId?.toInt() ?: 0
                    option2 = body?.nextSecondId?.toInt() ?: 0
                    currentBranch = body?.isCurrentBranch ?: false

                    // 선택지 분기 여부에 따라 버튼 텍스트 설정
                    updateButtons(currentBranch, body?.optionFirst, body?.optionSecond)

                    // TTS 재생
                    body?.processTTS?.let { ttsUrl ->
                        playTTS(ttsUrl)
                    }
                }
            }
        }
    }

    // MediaPlayer를 사용하여 TTS(mp3)를 재생하는 함수
    private fun playTTS(ttsUrl: String) {
        if (ttsUrl.isNotEmpty()) {
            mediaPlayer?.release() // 이전 재생 중인 미디어가 있으면 해제
            mediaPlayer = MediaPlayer().apply {
                setDataSource(ttsUrl)
                prepareAsync() // 비동기로 준비
                setOnPreparedListener {
                    start() // 재생 시작
                }
                setOnCompletionListener {
                    releaseMediaPlayer() // 재생 완료 시 MediaPlayer 해제
                }
            }
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer()
    }

    // 서버에서 온 이미지 Glide로 로드
    private fun setImageView(imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl) // 서버에서 받은 이미지 URL
                .into(binding.fragmentStoryIv) // 이미지를 로드할 ImageView
        }
    }

}