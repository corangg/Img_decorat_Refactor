package com.app.ui.fragment.sticker

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentStickerBinding
import com.app.recyclerview.EmojiGroupAdapter
import com.core.ui.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.presentation.StickerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StickerFragment : BaseFragment<FragmentStickerBinding>(FragmentStickerBinding::inflate) {
    private val viewModel: StickerViewModel by viewModels()
    override fun setUi() {}

    override fun setUpDate() {}

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.emojiData.observe(lifecycleOwner, ::setEmoji)
    }

    private fun setEmoji(emojiList: List<List<String>>) {
        if (emojiList.isEmpty()) {
            viewModel.downloadEmojiList()
        } else {
            adapterViewpager(emojiList)
        }
    }

    private fun adapterViewpager(emojiList: List<List<String>>) {
        binding.viewpager.adapter = EmojiGroupAdapter(requireActivity(), emojiList.size)
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            if (position < emojiList.size) {
                tab.text = emojiList[position][0]
            }
        }.attach()
    }
}