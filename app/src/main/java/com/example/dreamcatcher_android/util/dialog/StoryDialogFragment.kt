package com.example.dreamcatcher_android.util.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.databinding.DialogStoryBinding

class StoryDialogFragment(
    private val imageUrl: String,
    private var title: String,
    private var content: String,
    private val clickListener: DialogClickListener
) : DialogFragment() {

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        clickListener.onDialogDismiss() // 다이얼로그가 닫힐 때 MapFragment에서 핸들러를 다시 시작
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setGravity(Gravity.TOP)
        val params = dialog?.window?.attributes
        params?.y = 100 // 위쪽 마진을 100dp로 설정 (값을 조정하여 마진을 설정)
        dialog?.window?.attributes = params
    }

    // 뷰 바인딩
    private var _binding: DialogStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogStoryBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("다이얼로그", "title $title, content $content, img :$imageUrl")

        if (title.isEmpty()) {
            binding.dialogStoryTitleTv.text = "열기구 날자!"
        } else {
            binding.dialogStoryTitleTv.text = title
        }

        if (content.isEmpty()) {
            binding.dialogStoryContentTv.text = "열기구를 수리해서 드높은 하늘을 감상하자!"
        } else {
            binding.dialogStoryContentTv.text = content
        }

        // 확인 버튼 클릭
        binding.dialogStoryStartBt.setOnClickListener {
            clickListener.onBtnClick1()
            dismiss()
        }

        // 이미지 로드
        Glide.with(this)
            .load(imageUrl)
            .error("https://dreamcatcher-woody.s3.ap-northeast-2.amazonaws.com/parachute.png")
            .into(binding.dialogStoryPreviewIv)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
