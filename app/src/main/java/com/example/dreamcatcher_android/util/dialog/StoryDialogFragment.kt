package com.example.dreamcatcher_android.util.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.databinding.DialogStoryBinding

class StoryDialogFragment(private val imageUrl: String, title: String, content: String) :
    DialogFragment() {

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.7).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // 뷰 바인딩
    private var _binding: DialogStoryBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var content: String? = null

    init {
        this.title = title
        this.content = content
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogStoryBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogStoryTitleTv.text = title
        binding.dialogStoryContentTv.text = content

        // 확인 버튼 클릭
        binding.dialogStoryStartBt.setOnClickListener {
            dismiss()
        }

        Glide.with(this)
            .load(imageUrl)
            .into(binding.dialogStoryPreviewIv)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}