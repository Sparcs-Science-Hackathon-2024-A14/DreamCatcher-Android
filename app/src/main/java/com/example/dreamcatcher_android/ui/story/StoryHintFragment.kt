package com.example.dreamcatcher_android.ui.story

import android.media.MediaPlayer
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentStoryHintBinding
import com.example.dreamcatcher_android.ui.MainViewModel
import com.example.dreamcatcher_android.util.GlobalApplication
import kotlinx.coroutines.launch

// 스토리 분기 화면
class StoryHintFragment : BaseFragment<FragmentStoryHintBinding>(R.layout.fragment_story_hint) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var startQuestId = 0 // 첫 번째 퀘스트 아이디
    private var firstNextId = 0 // 첫 호출 시 사용되는 분기 호출 아이디
    private var option1 = 0 // 1번 선택지 ID
    private var option2 = 0 // 2번 선택지 ID
    private var mediaPlayer: MediaPlayer? = null // MediaPlayer 객체 추가


    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        getQuestId()
        getQuestProcess(firstNextId) // 첫 번째 분기 불러오기
        getStory()
        initButton()
    }

    // 퀘스트 불러오기 함수
    private fun getQuestProcess(nextId: Int) {  // 다음 분기 아이디
        mainViewModel.getQuestProcess(GlobalApplication.prefsManager.getUserId(), startQuestId, nextId)
    }

    private fun initButton() {

        // 선택지 1  id (유저), quest id, nextprocessid
        binding.fragmentStoryHintBt1.setOnClickListener {
            if(option1 != null) {
                getQuestProcess(option1)
                getStory()
            }
        }

        // 선택지 2
        binding.fragmentStoryHintBt2.setOnClickListener {
            if(option1 != null) {
                getQuestProcess(option2)
                getStory()
            }
        }
    }

    // nextFirstId가 null 이면 분기 종료
    private fun getStory() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.questResponse.collect { res ->
                    val body = res.body()
                    binding.fragmentStoryHintContentTv.text = body?.processDescription // 설명
                    setImageView(body?.processImg.toString()) // 이미지
                    binding.fragmentStoryHintBt1.text = body?.optionFirst // 첫 번째 선택지
                    binding.fragmentStoryHintBt2.text = body?.optionSecond // 두 번째 선택지
                    option1 = body?.nextFirstId?.toInt() ?: 0
                    option2 = body?.nextSecondId?.toInt() ?: 0
                    body?.processTTS?.let { ttsUrl ->
                        playTTS(ttsUrl)
                    }
                }
            }
        }
    }

    private fun getQuestId() {
        arguments?.let {
            startQuestId = it.getInt("startQuestId", 0) // 기본값 0 설정
            firstNextId = it.getInt("nextQuestId", 4)   // 기본값 0 설정
            Log.e("getQuestId", "startQuestId : $startQuestId ,,, firstNextId : $firstNextId")
        } ?: run {
            Log.e("MapFragment", "Arguments are null")
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

    private fun setImageView(imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl) // 서버에서 받은 이미지 URL
                .into(binding.fragmentStoryHintIv) // 이미지를 로드할 ImageView
        }
    }

}